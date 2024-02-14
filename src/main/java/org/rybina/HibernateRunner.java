package org.rybina;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.entity.Birthday;
import org.rybina.entity.Company;
import org.rybina.entity.PersonalInfo;
import org.rybina.entity.User;
import org.rybina.util.HibernateUtil;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        Company company = new Company();

//        User user = User.builder()
//                .personalInfo(PersonalInfo.builder()
//                        .lastName("Alina")
//                        .firstname("Rybina")
//                        .birthday(new Birthday(LocalDate.of(2004, 4, 12)))
//                        .build())
//                .username("rybinaaaa.a2")
//                .company(company)
//                .build();

        User user = null;

        log.info("User entity is in transient state, object {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {

                session.beginTransaction();
//                PersonalInfo userKey = PersonalInfo.builder()
//                        .lastName("Alina")
//                        .firstname("Rybina")
//                        .birthday(new Birthday(LocalDate.of(2004, 4, 12)))
//                        .build();

//                User user1 = session.get(User.class, user.getPersonalInfo());
//                System.out.println(user1);

//                user1.setCompany(new Company());

                session.getTransaction().commit();
            } catch (Exception exception) {
                log.error("Exception occurred", exception);
                throw exception;
            }
        }
    }
}
