package org.rybina;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.rybina.entity.Birthday;
import org.rybina.entity.User;
import org.rybina.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {

        User user = User.builder()
                .lastName("Alina")
                .firstname("Rybina")
                .username("rybinaaaa.a")
                .birthday(new Birthday(LocalDate.of(2004, 4, 12)))
                .build();

        log.info("User entity is in transient state, object {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                Transaction transaction = session1.beginTransaction();
                log.trace("Transaction is created {}", transaction);

                session1.saveOrUpdate(user);

                log.trace("User is in persistence state {}, session {}", user, session1);
                session1.getTransaction().commit();

                log.warn("User is in detached state {}, session is closed {}", user, session1);
            } catch (Exception exception) {
                log.error("Exception occured", exception);
                throw exception;
            }
        }
    }
}
