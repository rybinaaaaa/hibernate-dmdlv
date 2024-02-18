package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.entity.Payment;
import org.rybina.intercepror.GlobalInterceptor;
import org.rybina.util.HibernateUtil;
import org.rybina.util.TestDataImporter;

import javax.transaction.Transactional;
import java.sql.SQLException;

public class HibernateRunner {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            TestDataImporter.importData(sessionFactory);

            Payment payment = session.find(Payment.class, 2);
            payment.setAmount(payment.getAmount() + 10);

        }
    }
}


