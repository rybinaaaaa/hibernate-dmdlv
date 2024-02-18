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
             Session session = sessionFactory.openSession();
             Session session1 = sessionFactory.openSession()) {
//            TestDataImporter.importData(sessionFactory);

            session.beginTransaction();
            session1.beginTransaction();

            session.createQuery("from Payment ", Payment.class)
                    .setLockMode(LockModeType.PESSIMISTIC_READ)
                    .setHint("javax.persistence.lock.timeout", 5000)
                    .list();

//            OPTIMISTIC - обновляет внрсию сущности если мы в ней что-то меняем
//            OPTIMISTIC_FORCE_INCREMENT обновляет версию сущности в любом случае
//            PESSIMISTIC_READ
            Payment payment = session.find(Payment.class, 1, LockModeType.PESSIMISTIC_READ);
            payment.setAmount(payment.getAmount() + 10);

            Payment theSamePayment = session1.find(Payment.class, 1);
            theSamePayment.setAmount(payment.getAmount() + 15);

            session1.getTransaction().commit();
            session.getTransaction().commit();
        }
    }
}
