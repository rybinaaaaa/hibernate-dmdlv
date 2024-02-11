package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.rybina.entity.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration();
//        hibernate.cfg.xml by default
//        !!!
        configuration.addAnnotatedClass(User.class);
        configuration.configure();

//        sessionFactory == connectionPool
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession();
        ) {
            session.beginTransaction();

            User user = User.builder()
                    .lastName("lastN2")
                    .age(20)
                    .firstname("firstn")
                    .username("test3")
                    .birthday(LocalDate.of(2004, 2, 28))
                    .build();
            session.save(user);
            System.out.println("OK");

            session.getTransaction().commit();
        }
    }
}
