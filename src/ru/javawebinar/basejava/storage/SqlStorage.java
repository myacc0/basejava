package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.util.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        SqlHelper.runSql(connectionFactory, "DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume r) {
        SqlHelper.transactionalExecute(connectionFactory, conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, r.getUuid());
                ps.execute();
            }
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        SqlHelper.transactionalExecute(connectionFactory, conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        String sql =
                "   SELECT * FROM resume r " +
                        "   LEFT JOIN contact c " +
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
                addContacts(rs, r);
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
        String sql =
                "   SELECT * FROM resume r " +
                        "   LEFT JOIN contact c " +
                        "   ON c.resume_uuid = r.uuid";
        return SqlHelper.runSqlAndGetResult(connectionFactory, sql, ps -> {
            ResultSet rs = ps.executeQuery();
            Map<String, Resume> resumesMap = new HashMap<>();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                Resume r = resumesMap.get(uuid);
                if (r == null) {
                    r = new Resume(uuid, rs.getString("full_name"));
                    resumesMap.put(uuid, r);
                }
                addContacts(rs, r);
            }
            return resumesMap.values().stream().sorted(AbstractStorage.RESUME_COMPARATOR).toList();
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

    private void addContacts(ResultSet rs, Resume r) throws SQLException {
        if (rs == null) return;
        String value = rs.getString("value");
        ContactType type = ContactType.valueOf(rs.getString("type"));
        r.addContact(type, value);
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

}
