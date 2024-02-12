package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.rybina.entity.Birthday;
import org.rybina.entity.User;
import org.rybina.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class HibernateRunner {

    public static final Logger logger = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {

        User user = User.builder()
                .lastName("Alina")
                .firstname("Rybina")
                .username("rybinaaaa.a")
                .birthday(new Birthday(LocalDate.of(2004, 4, 12)))
                .build();

        logger.info("User entity is in transient state, object {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                Transaction transaction = session1.beginTransaction();
                logger.trace("Transaction is created {}", transaction);

                session1.saveOrUpdate(user);

                logger.trace("User is in persistence state {}, session {}", user, session1);
                session1.getTransaction().commit();

                logger.warn("User is in detached state {}, session is closed {}", user, session1);
            } catch (Exception exception) {
                logger.error("Exception occured", exception);
                throw exception;
            }
        }
    }
}
