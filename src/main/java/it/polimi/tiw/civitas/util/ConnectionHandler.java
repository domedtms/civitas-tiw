package it.polimi.tiw.civitas.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionHandler {

    private static final String DB_PROPERTIES_FILE = "db.properties";

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    private ConnectionHandler() {
        // Utility class: prevents instantiation.
    }

    static {
        loadDatabaseProperties();
    }

    private static void loadDatabaseProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(DB_PROPERTIES_FILE)) {

            if (inputStream == null) {
                throw new IllegalStateException("Database configuration file not found: " + DB_PROPERTIES_FILE);
            }

            properties.load(inputStream);

            dbUrl = properties.getProperty("db.url");
            dbUser = properties.getProperty("db.user");
            dbPassword = properties.getProperty("db.password");

            validateProperties();

        } catch (IOException e) {
            throw new IllegalStateException("Unable to load database configuration", e);
        }
    }

    private static void validateProperties() {
        if (isBlank(dbUrl)) {
            throw new IllegalStateException("Missing database property: db.url");
        }

        if (isBlank(dbUser)) {
            throw new IllegalStateException("Missing database property: db.user");
        }

        if (dbPassword == null) {
            throw new IllegalStateException("Missing database property: db.password");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
                // Nothing useful can be done here in this simple university project.
            }
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}