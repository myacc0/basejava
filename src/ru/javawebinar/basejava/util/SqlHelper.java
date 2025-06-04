package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.storage.SqlStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    public static void runSql(
            ConnectionFactory connectionFactory,
            String sql,
            SqlStorage.QueryExecutor executor
    ) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            executor.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public static <T> T runSqlAndGetResult(
            ConnectionFactory connectionFactory,
            String sql,
            SqlStorage.QuerySelectExecutor<T> executor
    ) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

}
