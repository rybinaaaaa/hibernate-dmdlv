package org.rybina;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.rybina.dao.PaymentRepository;
import org.rybina.entity.Payment;
import org.rybina.entity.User;
import org.rybina.util.HibernateUtil;

import javax.transaction.Transactional;
import java.lang.reflect.Proxy;
import java.util.List;


public class HibernateRunner {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            User user = null;
            try (Session session = sessionFactory.getCurrentSession()) {
                Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                        (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args));

                session.beginTransaction();

                PaymentRepository paymentRepository = new PaymentRepository(session);

                paymentRepository.findById(1).ifPresent(System.out::println);

                session.getTransaction().commit();
            }}
    }
}


