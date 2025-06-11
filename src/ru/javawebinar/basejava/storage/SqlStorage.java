package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
            String contactSql = "INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)";
            SqlHelper.runSql(connectionFactory, contactSql, ps -> {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.execute();
            });
        }
    }

    @Override
    public Resume get(String uuid) {
        String sql =
                "   SELECT * FROM resume r " +
                        "   LEFT JOIN contacts c " +
                        "       ON c.resume_uuid = r.uuid " +
                        "   WHERE r.uuid = ?";
        return SqlHelper.runSqlAndGetResult(connectionFactory, sql, ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                String value = rs.getString("value");
                ContactType type = ContactType.valueOf(rs.getString("type"));
                r.addContact(type, value);
            } while (rs.next());
            return r;
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
                                rs.getString("uuid"),
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
}
