package org.rybina;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.rybina.entity.Payment;
import org.rybina.util.HibernateUtil;
import org.rybina.util.TestDataImporter;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import javax.transaction.Transactional;

public class HibernateRunner {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
//            TestDataImporter.importData(sessionFactory);

            session.beginTransaction();

            Payment payment = session.find(Payment.class, 1);

            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }
}
