package org.example;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvValidationException;
import oracle.jdbc.internal.OracleTypes;

import javax.xml.crypto.Data;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.CSVReader.readOrdersFromCSV;



public class USLP06 {
    private static boolean lastSimulationWithPriorities;
    static String outputFilePath2;
    static String outputFilePath;


    public static void main(String[] args) throws SQLException {
        String csvFilePath = "/Users/diogogarrett/IdeaProjects/sem3-pi-2024_25_G145_v2/orders.csv";
        List<Order> orders = readOrdersFromCSV(csvFilePath);

        Map<String, Integer> operationTimes;
        try {
            operationTimes = fetchOperationTimesFromDatabase();
        } catch (SQLException e) {
            System.err.println("Failed to fetch operation times from the database: " + e.getMessage());
            return;
        }

        getArticles(orders);

        if (operationTimes.isEmpty()) {
            System.err.println("Operation times map is empty. Exiting.");
            return;
        }

        try (DatabaseConnection connection = DatabaseConnection.createConnection()) {
            simulateProduction(operationTimes, orders);
        } catch (SQLException e) {
            System.err.println("Error during simulation: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void getArticles(List<Order> orders) {
        outputFilePath2 = "orderArticles.csv";

        try (DatabaseConnection connection = DatabaseConnection.createConnection();
             CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(outputFilePath2))
                     .withSeparator(';')
                     .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                     .build()) {

            writer.writeNext(new String[]{
                    "article", "priority", "name_oper1", "name_oper2", "name_oper3",
                    "name_oper4", "name_oper5", "name_oper6"
            }.clone());

            for (Order order : orders) {
                try {
                    String query = "SELECT O.OP_TYPE " +
                            "FROM Operation O " +
                            "JOIN WorkstationTypes_Operation_Type WtOp ON WtOp.OP_TYPE = O.OP_TYPE " +
                            "WHERE O.ProductID = ? " +
                            "UNION " +
                            "SELECT O.OP_TYPE " +
                            "FROM Operation O " +
                            "JOIN BOO_OUTPUT BO ON O.OPID = BO.OPID " +
                            "JOIN WorkstationTypes_Operation_Type WtOp ON WtOp.OP_TYPE = O.OP_TYPE " +
                            "WHERE BO.PartNumber IN ( " +
                            "    SELECT PartNumber " +
                            "    FROM BOO_INPUT " +
                            "    WHERE OPID IN ( " +
                            "        SELECT OPID " +
                            "        FROM Operation " +
                            "        WHERE ProductID = ? " +
                            "    ) " +
                            ") " +
                            "ORDER BY 1";

                    List<String> operations = new ArrayList<>();
                    try (PreparedStatement stmt = connection.getInternalConnection().prepareStatement(query)) {
                        stmt.setString(1, order.getProductId());
                        stmt.setString(2, order.getProductId());
                        ResultSet rs = stmt.executeQuery();

                        while (rs.next() && operations.size() < 6) {
                            operations.add(rs.getString("OP_TYPE"));
                        }
                    }

                    // Criação de linhas adicionais se a quantidade for maior que 1
                    for (int qty = 1; qty <= order.getQuantity(); qty++) {
                        String[] row = new String[8];
                        row[0] = order.getProductId();
                        row[1] = order.getPriority();

                        for (int i = 0; i < Math.min(operations.size(), 6); i++) {
                            row[i + 2] = operations.get(i);
                        }

                        // Remove valores nulos
                        for (int i = 0; i < row.length; i++) {
                            if (row[i] == null) row[i] = "";
                        }

                        writer.writeNext(row);
                    }

                } catch (SQLException e) {
                    System.err.println("Error processing article " + order.getProductId() + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Error writing to 'orderArticles.csv': " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    /**
     * Reads orders from a CSV file.
     */


    /**
     * Connects to Oracle DB using ODBC and fetches workstation operation times.
     */
    public static Map<String, Integer> fetchOperationTimesFromDatabase() throws SQLException {
        Map<String, Integer> operationTimes = new HashMap<>();


        String query = "SELECT distinct WTID, (MaxTime + setuptime) as TotalTime FROM WorkstationTypes_Operation_Type";

        try (DatabaseConnection connection = DatabaseConnection.createConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String operationType = resultSet.getString("WTID");
                int maxTime = resultSet.getInt("TotalTime");
                operationTimes.put(operationType, maxTime);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return operationTimes;
    }

    /**
     * Simulates production using orders and operation times.
     */
    public static void simulateProduction(Map<String, Integer> operationTimes, List<Order> orders) throws SQLException {
        // Use the DatabaseConnection directly
        List<Article> articles = CSVReader.readArticlesFromCSV("./orderArticles.csv");
        List<Machine> machines = CSVReader.readMachinesFromCSV("./orderMachines.csv");
        DatabaseConnection dbConnection = DatabaseConnection.createConnection();

        Simulator simulator = new Simulator(articles, machines, dbConnection);
        simulator.runSimulation();
        lastSimulationWithPriorities = true;
        System.out.println("\nSimulation with priorities completed.");
    }


}