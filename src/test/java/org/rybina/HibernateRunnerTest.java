package org.rybina;

import org.junit.jupiter.api.Test;
import org.rybina.entity.User;

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
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.*;
import static java.util.stream.Collectors.*;

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
}
