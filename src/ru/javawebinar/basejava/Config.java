package ru.javawebinar.basejava;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final String PROPS = "/resources/resumes.properties";
    private static final Config INSTANCE = new Config();

    private Properties props = new Properties();
    private final File storageDir;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public static Config get() {
        return INSTANCE;
    }

    public Config() {
        try (InputStream is = Config.class.getResourceAsStream(PROPS)) {
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS);
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
