package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 * <p>
 * This class is specifically designed to establish a connection with an Oracle database using
 * the JDBC driver. The connection details such as URL, username, and password are hardcoded
 * for demonstration purposes.
 * <p>
 */
public class DatabaseConnection {

    /**
     * Establishes and returns a connection to the Oracle database.
     * <p>
     * The method uses the JDBC driver to connect to the Oracle database.
     * Ensure that the Oracle JDBC driver is available in the classpath.
     *
     * @return a {@code Connection} object representing the database connection
     * @throws SQLException if a database access error occurs or the JDBC driver is not found
     */
    public static Connection getConnection() throws SQLException {
        // Database connection URL (using service name instead of SID)
        String url = "jdbc:oracle:thin:@//localhost:1521/ORCLCDB";

        // Database credentials
        String user = "SYSTEM"; // Username
        String password = "mypassword1"; // Password

        try {
            // Register the Oracle JDBC driver
            Class.forName("oracle.jdbc.OracleDriver");

            // Establish the connection
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            // Handle the case where the Oracle JDBC driver is not available
            e.printStackTrace();
            throw new SQLException("Oracle JDBC Driver not found.", e);
        }
    }
}
