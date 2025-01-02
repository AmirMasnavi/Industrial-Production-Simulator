package org.example;

import java.sql.*;

/**
 * Utility class for managing database connections.
 * <p>
 * This class is specifically designed to establish a connection with an Oracle database using
 * the JDBC driver. The connection details such as URL, username, and password are hardcoded
 * for demonstration purposes.
 * <p>
 */
public class DatabaseConnection implements AutoCloseable {
    private Connection connection;

    public DatabaseConnection() throws SQLException {
        this.connection = getConnection();
    }

    public static DatabaseConnection createConnection() throws SQLException {
        return new DatabaseConnection();
    }

    static Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@//localhost:1521/ORCLCDB";
        String user = "SYSTEM";
        String password = "mypassword1";

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found.", e);
        }
    }

    public Connection getInternalConnection() {
        return connection;
    }

    public void executeUpdate(String query, Object[] params) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.executeUpdate();
        }
    }

    public ResultSet executeQuery(String query, Object[] params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public CallableStatement prepareCall(String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Connection is not established or is closed.");
        }
        return connection.prepareCall(query);
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }
}