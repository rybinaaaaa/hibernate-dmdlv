package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.entity.Birthday;
import org.rybina.entity.User;
import org.rybina.util.HibernateUtil;

import java.sql.SQLException;
import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        User user = User.builder()
                .lastName("Alina")
                .firstname("Rybina")
                .username("rybinaaaa.a")
                .birthday(new Birthday(LocalDate.of(2004, 4, 12)))
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {

                session1.beginTransaction();

                session1.getTransaction().commit();

                session1.beginTransaction();

                session1.saveOrUpdate(user);
                user.setFirstname("kkk");

                System.out.println(session1.isDirty());

                session1.getTransaction().commit();
            }

            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

//                session2.delete(user);

                session2.getTransaction().commit();
            }
        }
    }
}
