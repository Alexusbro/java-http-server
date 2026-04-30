package ru.otus.java.basic.httpserver.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class.getName());
    private Connection connection;
    private Statement stmt;

    public Connection getConnection() {
        return connection;
    }

    public DatabaseConnection(String url, String user, String password) {
        connect(url, user, password);
        createTable();
    }

    private void connect(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            stmt = connection.createStatement();
        } catch (SQLException e) {
            logger.warn("failed connection database" + e);
        }
    }

    private void createTable() {
        try {
            stmt.executeUpdate("create table if not exists book_catalog (" +
                    "id bigserial primary key," +
                    "author varchar(255)," +
                    "title varchar(255)" +
                    ")");
        } catch (SQLException e) {
            logger.warn("failed create database" + e);
        }
    }

}
