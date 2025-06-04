package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        SqlHelper.runSql(connectionFactory, "DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        String sql = "UPDATE resume SET full_name = ? WHERE uuid = ?";
        SqlHelper.runSql(connectionFactory, sql, ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
        });
    }

    @Override
    public void save(Resume r) {
        String sql = "INSERT INTO resume (uuid, full_name) VALUES (?, ?)";
        SqlHelper.runSql(connectionFactory, sql, ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
        });
    }

    @Override
    public Resume get(String uuid) {
        String sql = "SELECT * FROM resume r WHERE r.uuid = ?";
        return SqlHelper.runSqlAndGetResult(connectionFactory, sql, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        });
    }

    @Override
    public void delete(String uuid) {
        String sql = "DELETE FROM resume r WHERE r.uuid = ?";
        SqlHelper.runSql(connectionFactory, sql, ps -> {
            ps.setString(1, uuid);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new NotExistStorageException(uuid);
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        String sql = "SELECT * FROM resume r ORDER BY r.full_name asc, r.uuid asc";
        return SqlHelper.runSqlAndGetResult(connectionFactory, sql, ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> resumes = new ArrayList<>();
            while (rs.next()) {
                resumes.add(
                        new Resume(
                                rs.getString("uuid").trim(),
                                rs.getString("full_name")));
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        String sql = "SELECT count(*) as total FROM resume";
        return SqlHelper.runSqlAndGetResult(connectionFactory, sql, ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new StorageException("Query execution error, unexpected resultSet");
            }
            return rs.getInt("total");
        });
    }

    public interface QueryExecutor {
        void execute(PreparedStatement ps) throws SQLException;
    }

    public interface QuerySelectExecutor<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }
}
