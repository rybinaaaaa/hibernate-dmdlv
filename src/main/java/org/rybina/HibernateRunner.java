package org.rybina;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.rybina.convertor.BirthdayConvertor;
import org.rybina.entity.Birthday;
import org.rybina.entity.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration();
//        hibernate.cfg.xml by default
//        !!!
        configuration.addAnnotatedClass(User.class);
//        configuration.addAttributeConverter(new BirthdayConvertor(), true);
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();

//        sessionFactory == connectionPool
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession();
        ) {
            session.beginTransaction();

            User user = User.builder()
                    .lastName("lastN2")
                    .firstname("firstn")
                    .username(LocalDateTime.now().toString())
                    .birthday(new Birthday(LocalDate.of(2004, 2, 28)))
                    .info("""
                            {
                                "name": "ja",
                                "id": 25
                            }""")
                    .build();
            session.save(user);
            System.out.println("OK");

            session.getTransaction().commit();
        }
    }
}
