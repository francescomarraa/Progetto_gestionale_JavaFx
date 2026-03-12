package org.example.progettouid.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:aziendale.db";

    private static DatabaseHelper instance;
    private Connection connection;

    private DatabaseHelper() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTable();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore di connessione al Database!");
        }
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "email VARCHAR(100) PRIMARY KEY, " +
                "password VARCHAR(100));";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        }


    }
}