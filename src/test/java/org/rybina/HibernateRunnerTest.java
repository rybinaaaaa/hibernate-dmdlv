package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.rybina.entity.Company;
import org.rybina.entity.User;
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
import java.util.Arrays;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

public class HibernateRunnerTest {

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
}
