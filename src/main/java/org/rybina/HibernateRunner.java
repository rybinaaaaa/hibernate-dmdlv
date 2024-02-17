package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.entity.User;
import org.rybina.util.HibernateUtil;

public class HibernateRunner {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();

            User user = session.get(User.class, 1);

            session.getTransaction().commit();
        }
    }
}
