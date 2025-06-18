package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.util.JsonParser;
import ru.javawebinar.basejava.util.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
                ps.setString(1, r.getUuid());
                ps.execute();
            }
            insertContacts(conn, r);
            insertSections(conn, r);
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
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        Resume resume = SqlHelper.runSqlAndGetResult(
                connectionFactory,
                "SELECT * FROM resume r WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return new Resume(uuid, rs.getString("full_name"));
                });

        SqlHelper.runSql(connectionFactory, "SELECT * FROM contact c WHERE c.resume_uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                addContact(rs, resume);
            }
        });

        SqlHelper.runSql(connectionFactory, "SELECT * FROM section s WHERE s.resume_uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                addSection(rs, resume);
            }
        });
        return resume;
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
        Map<String, Resume> resumes = new HashMap<>();
        SqlHelper.runSql(connectionFactory, "SELECT * FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
            }
        });

        SqlHelper.runSql(connectionFactory, "SELECT * FROM contact", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                addContact(rs, resumes.get(rs.getString("resume_uuid")));
            }
        });

        SqlHelper.runSql(connectionFactory, "SELECT * FROM section", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                addSection(rs, resumes.get(rs.getString("resume_uuid")));
            }
        });
        return resumes.values().stream().sorted(AbstractStorage.RESUME_COMPARATOR).toList();
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

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        if (rs == null || r == null) return;
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

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        if (rs == null || r == null) return;
        String value = rs.getString("value");
        SectionType type = SectionType.valueOf(rs.getString("type"));
        switch (type) {
            case OBJECTIVE, PERSONAL -> {
                r.addSection(type, new TextSection(value));
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                List<String> content = (value != null)
                        ? Arrays.asList(value.split("\n"))
                        : Collections.emptyList();
                r.addSection(type, new ListSection(content));
            }
            case EXPERIENCE, EDUCATION -> {
                r.addSection(type, JsonParser.parseJson(value, OrganizationSection.class));
            }
        }
    }

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());

                switch (e.getKey()) {
                    case OBJECTIVE, PERSONAL -> {
                        ps.setString(3, ((TextSection) e.getValue()).getContent());
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ps.setString(3, String.join("\n", ((ListSection) e.getValue()).getContent()));
                    }
                    case EXPERIENCE, EDUCATION -> {
                        ps.setString(3, JsonParser.toJson(e.getValue()));
                    }
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

}
