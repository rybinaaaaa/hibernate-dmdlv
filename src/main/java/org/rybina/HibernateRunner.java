package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration();
//        hibernate.cfg.xml by default
        configuration.configure();

//        sessionFactory == connectionPool
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession();
        ) {
            System.out.println("OK");
        }
    }
}
