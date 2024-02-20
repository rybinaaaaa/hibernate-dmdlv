package org.rybina;

import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.rybina.entity.*;
import org.rybina.util.HibernateTestUtil;
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
import java.util.Map;

import static java.time.Instant.now;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

public class HibernateRunnerTest {

//    List<User> getUsers() {
//        User user1 = User.builder()
//                .personalInfo(PersonalInfo.builder()
//                        .firstname("John")
//                        .lastName("Doe")
//                        .birthday(new Birthday(LocalDate.of(1990, 1, 1)))
//                        .build())
//                .username("johndoe90")
//                .info("{\"hobbies\":[\"football\",\"reading\"]}")
//                .build();
//
//        // Creating the second user
//        User user2 = User.builder()
//                .personalInfo(PersonalInfo.builder()
//                        .firstname("Alex")
//                        .lastName("Smith")
//                        .birthday(new Birthday(LocalDate.of(1985, 5, 15)))
//                        .build())
//                .username("alexsmith85")
//                .info("{\"hobbies\":[\"swimming\",\"traveling\"]}")
//                .build();
//
//        // Creating the third user
//        User user3 = User.builder()
//                .personalInfo(PersonalInfo.builder()
//                        .firstname("Maria")
//                        .lastName("Johnson")
//                        .birthday(new Birthday(LocalDate.of(1995, 3, 25)))
//                        .build())
//                .username("mariajohnson95")
//                .info("{\"hobbies\":[\"painting\",\"music\"]}")
//                .build();
//        return List.of(user1, user2, user3);
//    }

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
//        User user = User.builder().build();
        User user = null;

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

//            User user = User.builder().username("Kika")
//                    .build();
//
            User user = null;

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
        Map<String, User> users = company.getUsers();

        users.forEach((k, v) -> System.out.println(v));
    }

    @Test
    void checkOrphanRemoval() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()
        ) {
            session.beginTransaction();

            Company company = session.getReference(Company.class, 10);
//            company.getUsers().removeIf(user -> user.getId().equals(16));

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

            User user = null;

            Profile profile = Profile.builder()
                    .language("ua")
                    .street("Chaloupeckeho")
                    .build();

            profile.setUser(user);
            session.save(user);
//            profile.setUser(user);
//            User user = session.get(User.class, 22);
//            System.out.println();

            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToMany() {
        try (
                SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
                Session session = sessionFactory.openSession()
        ) {
            session.beginTransaction();

            User user = session.get(User.class, 22);

            Chat chat = Chat.builder()
                    .name("spring")
                    .build();

            session.save(chat);

            UserChat userChat = UserChat.builder()
                    .createdAt(now())
                    .createdBy(user.getUsername())
                    .build();

            userChat.setUser(user);
            userChat.setChat(chat);
//
//
//            session.save(chat);

            session.getTransaction().commit();
        }
    }

    @Test
    void localeInfo() {
        try (
                SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
                Session session = sessionFactory.openSession()
        ) {
            session.beginTransaction();

            Company company = session.get(Company.class, 10);

//            getUsers().forEach(company::addUser);

            company.getUsers().forEach((k, v) -> System.out.println(v));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkH2() {
        try (
                SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
                Session session = sessionFactory.openSession()
        ) {
            session.beginTransaction();

            session.get(Company.class, 10);

            session.save(Company.builder().name("twitter").build());

            session.getTransaction().commit();
        }
    }

//    @Test
//    void checkInheritancePerTable() {
//        try (
//                SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
//                Session session = sessionFactory.openSession()
//        ) {
//            session.beginTransaction();
//
//            session.get(CompanyRepository.class, 10);
//
//            Programmer programmer = Programmer.builder()
//                    .personalInfo(PersonalInfo.builder()
//                            .firstname("Alina")
//                            .lastName("Rybina")
//                            .birthday(LocalDate.of(1995, 3, 25))
//                            .build())
//                    .username("rybinaaa.a")
////                    .info("{\"hobbies\":[\"painting\",\"music\"]}")
//                    .language(Language.JAVA)
//                    .build();
//
//            session.save(programmer);
//
//            Manager manager = Manager.builder()
//                    .personalInfo(PersonalInfo.builder()
//                            .firstname("Danya")
//                            .lastName("Bykov")
//                            .birthday(new Birthday(LocalDate.of(1995, 3, 25)))
//                            .build())
//                    .username("danil.o")
//                    .info("{\"hobbies\":[\"painting\",\"music\"]}")
//                    .projectName("appleMania")
//                    .build();
//
//            session.save(manager);
//
//            session.flush();
//
//            session.clear();
//
////            Programmer programmer1 = session.get(Programmer.class, 1);
////            Manager manager1 = session.get(Manager.class, 2);
//
//
//            User user = session.get(User.class, 2);
//
//            System.out.println();
//
//            session.getTransaction().commit();
//        }
//    }

    @Test
    void chackHql() {
        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()
        ) {
            session.beginTransaction();

//            JPQL, HQL
//            List<User> list = session.createQuery(
//                    "select  u from User u where u.personalInfo.firstname = ?1", User.class)
//                    .setParameter(1, "Maria")
//                    .list();

//            List<User> list = session.createQuery(
//                    "select  u from User u " +
//                    "join u.company c " +
//                    "where u.personalInfo.firstname = :firstname", User.class)
//                    .setParameter("firstname", "Maria")
//                    .list();

            List<User> list = session.createNamedQuery(
                            "findUserByName", User.class)
                    .setParameter("firstname", "Maria")
                    .setHint(QueryHints.HINT_FETCH_SIZE, 50)
                    .setHibernateFlushMode(FlushMode.COMMIT)
                    .list();

//            Eq. to query.getResultList
//            query.list()

            session.getTransaction().commit();
        }
    }
}



//Template

//    @Test
//    void ?() {
//        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
//             Session session = sessionFactory.openSession()
//        ) {
//            session.beginTransaction();
//
//            session.getTransaction().commit();
//        }
//    }