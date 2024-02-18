package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.entity.Payment;
import org.rybina.util.HibernateUtil;
import org.rybina.util.TestDataImporter;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

public class HibernateRunner {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
//            TestDataImporter.importData(sessionFactory);

            session.beginTransaction();

//            OPTIMISTIC - обновляет внрсию сущности если мы в ней что-то меняем
//            OPTIMISTIC_FORCE_INCREMENT обновляет версию сущности в любом случае
//            PESSIMISTIC_READ
            Payment payment = session.find(Payment.class, 1, LockModeType.PESSIMISTIC_READ);
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }
}
