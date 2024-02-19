package org.rybina;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.rybina.entity.Payment;
import org.rybina.entity.User;
import org.rybina.intercepror.GlobalInterceptor;
import org.rybina.util.HibernateUtil;
import org.rybina.util.TestDataImporter;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.SQLException;

public class HibernateRunner {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

//                TestDataImporter.importData(sessionFactory);

                User user = session.find(User.class, 2);
                user.setUsername(user.getUsername() + "test");

                session.getTransaction().commit();
            }
        }
    }
}


