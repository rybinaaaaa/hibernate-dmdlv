package org.rybina;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.rybina.entity.*;
import org.rybina.util.HibernateUtil;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

public class HibernateRunnerTest {

    List<User> getUsers() {
        User user1 = User.builder()
                .personalInfo(PersonalInfo.builder()
                        .firstname("John")
                        .lastName("Doe")
                        .birthday(new Birthday(LocalDate.of(1990, 1, 1)))
                        .build())
                .username("johndoe90")
                .info("{\"hobbies\":[\"football\",\"reading\"]}")
                .build();

        // Creating the second user
        User user2 = User.builder()
                .personalInfo(PersonalInfo.builder()
                        .firstname("Alex")
                        .lastName("Smith")
                        .birthday(new Birthday(LocalDate.of(1985, 5, 15)))
                        .build())
                .username("alexsmith85")
                .info("{\"hobbies\":[\"swimming\",\"traveling\"]}")
                .build();

        // Creating the third user
        User user3 = User.builder()
                .personalInfo(PersonalInfo.builder()
                        .firstname("Maria")
                        .lastName("Johnson")
                        .birthday(new Birthday(LocalDate.of(1995, 3, 25)))
                        .build())
                .username("mariajohnson95")
                .info("{\"hobbies\":[\"painting\",\"music\"]}")
                .build();
        return List.of(user1, user2, user3);
    }

    @Test
    void checkGetReflectionApi() throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.getString("username");
        resultSet.getString("lastname");
        resultSet.getString("firstname");

        Class<User> clazz = User.class;
        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();

        Field declaredField = clazz.getDeclaredField("username");
        declaredField.setAccessible(true);
        declaredField.set(user, resultSet.getString("username"));
    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder().build();

        String sql = """
                insert into %s (%s) values (%s)
                """;
        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(table -> table.schema() + "." + table.name()).orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields).map(field -> ofNullable(field.getAnnotation(Column.class)).map(Column::name).orElse(field.getName())).collect(joining(","));

        String columnValues = Arrays.stream(declaredFields).map(field -> "?").collect(joining(","));

        String readySqlQuery = sql.formatted(tableName, columnNames, columnValues);

        System.out.println(readySqlQuery);

        Connection connection = null;

        PreparedStatement preparedStatement = connection.prepareStatement(readySqlQuery);
        int index = 0;
        for (Field declaredField : declaredFields) {
            index++;
            declaredField.setAccessible(true);
            preparedStatement.setObject(index, declaredField.get(user));
        }
    }

    @Test
    void oneToMany() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            Company company = session.get(Company.class, 8);

            System.out.println(company);

            session.getTransaction().commit();
        }
    }

    @Test
    void addUserToNewCompany() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()
        ) {

            session.beginTransaction();

            Company company = Company.builder()
                    .name("facebook")
                    .build();

            User user = User.builder().username("Kika")
                    .build();

            company.addUser(user);

            session.save(company);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialization() {
        Company company = null;

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()
        ) {

            session.beginTransaction();


            company = session.get(Company.class, 10);

            Hibernate.initialize(company.getUsers());

            session.getTransaction().commit();
        }
        List<User> users = company.getUsers();

        users.forEach(System.out::println);
    }

    @Test
    void checkOrphanRemoval() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()
        ) {
            session.beginTransaction();

            Company company = session.getReference(Company.class, 10);
            company.getUsers().removeIf(user -> user.getId().equals(16));

            System.out.println(session.isDirty());
            session.getTransaction().commit();
            System.out.println();
        }
    }

    @Test
    void checkOneToOne() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()
        ) {
            session.beginTransaction();

//            User user = getUsers().get(0);
//
//            Profile profile = Profile.builder()
//                    .language("ua")
//                    .street("Chaloupeckeho")
//                    .build();
//
//            session.save(user);
//
//            profile.setUser(user);

            User user = session.get(User.class, 22);
            System.out.println();

            session.getTransaction().commit();
        }
    }
}
