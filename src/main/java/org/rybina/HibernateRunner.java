package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.util.HibernateUtil;

import javax.transaction.Transactional;

public class HibernateRunner {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.doWork(connection -> System.out.println(connection.getTransactionIsolation()));
        }
    }
}
