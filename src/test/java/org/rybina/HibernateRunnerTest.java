package org.rybina;

import org.junit.jupiter.api.Test;
import org.rybina.entity.User;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.*;
import static java.util.stream.Collectors.*;

public class HibernateRunnerTest {

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
