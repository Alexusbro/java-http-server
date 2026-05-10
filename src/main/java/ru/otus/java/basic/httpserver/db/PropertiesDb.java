package ru.otus.java.basic.httpserver.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesDb {
    private static final Logger logger = LogManager.getLogger(PropertiesDb.class.getName());
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public PropertiesDb() {
        try (FileInputStream fileInput = new FileInputStream("db.properties")) {
            Properties properties = new Properties();
            properties.load(fileInput);
            this.dbUrl = properties.getProperty("db.url");
            this.dbUser = properties.getProperty("db.user");
            this.dbPassword = properties.getProperty("db.password");
            logger.info("configuration for database successful load");
        } catch (IOException e) {
            logger.warn("failed to load configuration database");
        }

    }
}