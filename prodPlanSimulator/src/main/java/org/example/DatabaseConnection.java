package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        // URL de conexão com o banco de dados Oracle
        // Use o nome do serviço (ORCLOCDB) em vez do SID para a conexão.
        String url = "jdbc:oracle:thin:@//localhost:1521/ORCLCDB";
        String user = "SYSTEM"; // O nome de usuário é SYSTEM
        String password = "mypassword1"; // A senha é 'mypassword1'

        try {
            // Registrar o driver JDBC Oracle
            Class.forName("oracle.jdbc.OracleDriver");
            // Estabelecer a conexão com o banco de dados
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Oracle JDBC Driver not found.", e);
        }
    }
}
