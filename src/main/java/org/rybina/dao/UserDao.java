package org.rybina.dao;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.rybina.dto.PaymentFilter;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
//
//    private static final UserDao INSTANCE = new UserDao();
//
//    public List<User> findAll(Session session) {
////        return session.createQuery("from User", User.class).list();
//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(user)
//                .fetch();
//    }
//
//    /**
//     * Возвращает всех сотрудников с указанным именем
//     */
//    public List<User> findAllByFirstName(Session session, String firstName) {
////        return session.createQuery("select u from User u where u.personalInfo.firstname = :firstName", User.class)
////                .setParameter("firstName", firstName)
////                .list();
//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(user)
//                .where(user.personalInfo.firstname.eq(firstName))
//                .fetch();
//    }
//
//    /**
//     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
//     */
//    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
////        return session.createQuery("from User u order by u.personalInfo.birthday", User.class)
////                .setMaxResults(limit)
////                .list();
//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(user)
//                .orderBy(user.personalInfo.birthday.asc())
//                .limit(limit)
//                .fetch();
//    }
//
//    /**
//     * Возвращает всех сотрудников компании с указанным названием
//     */
//    public List<User> findAllByCompanyName(Session session, String companyName) {
////        return session.createQuery("from User u where u.company.name = :companyName", User.class)
////                .setParameter("companyName", companyName)
////                .list();
//        return new JPAQuery<User>(session)
//                .select(user)
//                .from(company)
//                .join(company.users, user)
//                .where(company.name.eq(companyName))
//                .fetch();
//    }
//
//    /**
//     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
//     * упорядоченные по имени сотрудника, а затем по размеру выплаты
//     */
//    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
////        return session.createQuery("from Payment p " +
////                                   "where p.receiver.company.name = :companyName " +
////                                   "order by p.receiver.personalInfo.firstname, p.amount", Payment.class)
////                .setParameter("companyName", companyName)
////                .list();
//        return new JPAQuery<Payment>(session)
//                .select(payment)
//                .from(payment)
////                alias
//                .join(payment.receiver, user)
//                .join(user.company, company)
//                .where(company.name.eq(companyName))
//                .orderBy(user.personalInfo.firstname.asc(), payment.amount.asc())
//                .fetch();
//
//    }
//
//    /**
//     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
//     */
//    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, PaymentFilter filter) {
////        return session.createQuery("select avg(amount) from Payment p " +
////                                   "where p.receiver.personalInfo.firstname = :firstName " +
////                                   "and p.receiver.personalInfo.lastName = :lastName", Double.class)
////                .setParameter("firstName", firstName)
////                .setParameter("lastName", lastName)
////                .uniqueResult();
//
//        Predicate predicate = QPredicate.builder()
//                .add(filter.getFirstName(), payment.receiver.personalInfo.firstname::eq)
//                .add(filter.getLastName(), payment.receiver.personalInfo.lastName::eq)
//                .buildAnd();
//
//        return new JPAQuery<Double>(session)
//                .select(payment.amount.avg())
//                .from(payment)
//                .where(predicate)
//                .fetchOne();
//    }
//
//    /**
//     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
//     */
//    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
////        return session.createQuery("select c.name, avg(p.amount) as salary from Payment p " +
////                                   "join p.receiver.company c " +
////                                   "group by c order by c.name", Object[].class)
////                .list();
//        return new JPAQuery<Tuple>(session)
//                .select(company.name, payment.amount.avg().as("salary"))
//                .from(payment)
//                .join(payment.receiver.company, company)
//                .groupBy(company.name)
//                .orderBy(company.name.asc())
//                .fetch();
//    }
//
//    /**
//     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
//     * больше среднего размера выплат всех сотрудников
//     * Упорядочить по имени сотрудника
//     */
//    public List<Tuple> isItPossible(Session session) {
////        return session.createQuery("select u, avg(p.amount) from User u " +
////                                   "join u.payments p " +
////                                   "group by u " +
////                                   "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
////                                   "order by u.personalInfo.firstname", Object[].class)
////                .list();
//        return new JPAQuery<Tuple>(session)
//                .select(user, payment.amount.avg())
//                .from(user)
//                .join(user.payments, payment)
//                .groupBy(user.id)
//                .having(payment.amount.avg().gt(new JPAQuery<Double>(session)
//                        .select(payment.amount.avg())
//                        .from(payment)
//                ))
//                .orderBy(user.personalInfo.firstname.asc())
//                .fetch();
//    }
//
//    public static UserDao getInstance() {
//        return INSTANCE;
//    }
}