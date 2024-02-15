package org.rybina.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.rybina.dto.CompanyDto;
import org.rybina.entity.*;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    public List<User> findAll(Session session) {
//        return session.createQuery("from User", User.class).list();
        CriteriaBuilder cb = session.getCriteriaBuilder();

//        еш, что мы возвращаем
        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot);

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
//        return session.createQuery("select u from User u where u.personalInfo.firstname = :firstName", User.class)
//                .setParameter("firstName", firstName)
//                .list();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot)
                .where(cb.equal(userRoot.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
//        return session.createQuery("from User u order by u.personalInfo.birthday", User.class)
//                .setMaxResults(limit)
//                .list();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<User> userRoot = criteria.from(User.class);

        criteria.select(userRoot).orderBy(cb.asc(userRoot.get(User_.personalInfo).get(PersonalInfo_.birthday)));

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
//        return session.createQuery("from User u where u.company.name = :companyName", User.class)
//                .setParameter("companyName", companyName)
//                .list();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<Company> company = criteria.from(Company.class);

        MapJoin<Company, String, User> users = company.join(Company_.users);

        criteria.select(users).where(cb.equal(company.get(Company_.name), companyName));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
//        return session.createQuery("from Payment p " +
//                                   "where p.receiver.company.name = :companyName " +
//                                   "order by p.receiver.personalInfo.firstname, p.amount", Payment.class)
//                .setParameter("companyName", companyName)
//                .list();
        var cb = session.getCriteriaBuilder();
        CriteriaQuery<Payment> criteria = cb.createQuery(Payment.class);

        Root<Payment> paymentRoot = criteria.from(Payment.class);

        var userJoin = paymentRoot.join(Payment_.receiver);
        var company = userJoin.join(User_.company);

        criteria.select(paymentRoot).where(cb.equal(company.get(Company_.name), companyName))
                .orderBy(cb.asc(userJoin.get(User_.personalInfo).get(PersonalInfo_.firstname)),
                        cb.asc(paymentRoot.get(Payment_.amount))
                );

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
//        return session.createQuery("select avg(amount) from Payment p " +
//                                   "where p.receiver.personalInfo.firstname = :firstName " +
//                                   "and p.receiver.personalInfo.lastName = :lastName", Double.class)
//                .setParameter("firstName", firstName)
//                .setParameter("lastName", lastName)
//                .uniqueResult();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);

        var payment = criteria.from(Payment.class);

        var user = payment.join(Payment_.receiver);

        criteria.select(cb.avg(payment.get(Payment_.amount)))
                .where(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName),
                        cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastName), lastName));

        return session.createQuery(criteria)
                .uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
//        return session.createQuery("select c.name, avg(p.amount) as salary from Payment p " +
//                                   "join p.receiver.company c " +
//                                   "group by c order by c.name", Object[].class)
//                .list();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<CompanyDto> criteria = cb.createQuery(CompanyDto.class);

        Root<Company> comp = criteria.from(Company.class);

        var user = comp.join(Company_.users, JoinType.INNER);
        var payment = user.join(User_.payments);

//        criteria.multiselect(
        criteria.select(
                cb.construct(CompanyDto.class,
                        comp.get(Company_.name),
                        cb.avg(payment.get(Payment_.amount))
                        )
//                comp.get(Company_.name),
//                cb.avg(payment.get(Payment_.amount))
        )
                .groupBy(comp.get(Company_.name))
                .orderBy(cb.asc(comp.get(Company_.name)));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Tuple> isItPossible(Session session) {
//        return session.createQuery("select u, avg(p.amount) from User u " +
//                                   "join u.payments p " +
//                                   "group by u " +
//                                   "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
//                                   "order by u.personalInfo.firstname", Object[].class)
//                .list();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        var user = criteria.from(User.class);
        var payments = user.join(User_.payments);

//        analog createQuery
        var subquery = criteria.subquery(Double.class);
        var paymentSubquery = subquery.from(Payment.class);

        criteria.select(
                cb.tuple(user, cb.avg(payments.get(Payment_.amount)))
                )
                .groupBy(user.get(User_.id))
                .having(cb.gt(cb.avg(payments.get(Payment_.amount)), subquery.select(cb.avg(paymentSubquery.get(Payment_.amount)))))
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));

        return session.createQuery(criteria)
                .list();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}