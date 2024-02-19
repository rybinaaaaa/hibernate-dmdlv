package org.rybina;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.rybina.entity.Payment;
import org.rybina.entity.User;
import org.rybina.util.HibernateUtil;

import javax.transaction.Transactional;


public class HibernateRunner {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            User user = null;
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

//                TestDataImporter.importData(sessionFactory);

                user = session.find(User.class, 2);
                User user1 = session.find(User.class, 2);

                session.getTransaction().commit();
            }
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

//                TestDataImporter.importData(sessionFactory);

                User user2 = session.find(User.class, 2);

                session.getTransaction().commit();
            }
        }
    }
}


