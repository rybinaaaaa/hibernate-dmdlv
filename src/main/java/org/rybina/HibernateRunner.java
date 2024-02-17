package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.entity.User;
import org.rybina.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class HibernateRunner {

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();

            users = session.createQuery("from User", User.class).list();

//            users.forEach(u -> u.getPayments().forEach(payment -> payment.getId()));
            users.get(0).getPayments().forEach(payment -> payment.getId());
            session.getTransaction().commit();
        }
        users.forEach(u-> System.out.println(u.getPayments()));
    }
}
