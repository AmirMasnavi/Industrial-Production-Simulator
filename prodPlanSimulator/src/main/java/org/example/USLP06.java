package org.example;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class USLP06 {
    static String outputFilePath2;  // Output file path for order articles

    /**
     * Method to retrieve articles from orders and write their operations to a CSV file.
     * @param orders List of Order objects representing customer orders.
     */
    private static void getArticles(List<Order> orders) {
        outputFilePath2 = "orderArticles.csv";  // Setting the file name for output

        try (DatabaseConnection connection = DatabaseConnection.createConnection();  // Create database connection
             CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(outputFilePath2))  // Create CSVWriter object
                     .withSeparator(';')  // Set CSV separator as semicolon
                     .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)  // Avoid quoting fields
                     .build()) {

            // Write the header of the CSV file
            writer.writeNext(new String[]{
                    "article", "priority", "name_oper1", "name_oper2", "name_oper3",
                    "name_oper4", "name_oper5", "name_oper6"
            }.clone());

            // Iterate over each order and fetch associated operations
            for (Order order : orders) {
                try {
                    // SQL query to fetch operation types based on ProductID from the database
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
                    // Prepare statement for SQL query and fetch results
                    try (PreparedStatement stmt = connection.getInternalConnection().prepareStatement(query)) {
                        stmt.setString(1, order.getProductId());
                        stmt.setString(2, order.getProductId());
                        ResultSet rs = stmt.executeQuery();

                        // Collect operation types for the given product
                        while (rs.next() && operations.size() < 6) {
                            operations.add(rs.getString("OP_TYPE"));
                        }
                    }

                    // Create rows in CSV for each quantity of the order
                    for (int qty = 1; qty <= order.getQuantity(); qty++) {
                        String[] row = new String[8];
                        row[0] = order.getProductId();  // Article/Product ID
                        row[1] = order.getPriority();   // Priority of the order

                        // Add operations to the row (up to 6 operations)
                        for (int i = 0; i < Math.min(operations.size(), 6); i++) {
                            row[i + 2] = operations.get(i);
                        }

                        // Fill empty spaces with empty strings to avoid null values in CSV
                        for (int i = 0; i < row.length; i++) {
                            if (row[i] == null) row[i] = "";
                        }

                        // Write the row to the CSV file
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
     * Fetches operation times from the database for workstations.
     * @return A map containing operation types and their corresponding time values.
     */
    public static Map<String, Integer> fetchOperationTimesFromDatabase() throws SQLException {
        Map<String, Integer> operationTimes = new HashMap<>();

        // SQL query to fetch operation times and workstation types from the database
        String query = "SELECT distinct WTID, (MaxTime + setuptime) as TotalTime FROM WorkstationTypes_Operation_Type";

        try (DatabaseConnection connection = DatabaseConnection.createConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Process each row from the query result
            while (resultSet.next()) {
                String operationType = resultSet.getString("WTID");
                int maxTime = resultSet.getInt("TotalTime");
                operationTimes.put(operationType, maxTime);  // Store operation times in map
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return operationTimes;
    }

    /**
     * Simulates the production process using orders and operation times.
     * The simulation considers articles, machines, and workstation operations.
     */
    public static void simulateProduction() throws SQLException {
        // Read articles and machines from CSV files
        List<Article> articles = CSVReader.readArticlesFromCSV("./orderArticles.csv");
        List<Machine> machines = CSVReader.readMachinesFromCSV("./orderMachines.csv");

        // Create a database connection for the simulation
        DatabaseConnection dbConnection = DatabaseConnection.createConnection();

        // Initialize the simulator with articles, machines, and database connection
        Simulator simulator = new Simulator(articles, machines, dbConnection);

        // Run the simulation
        simulator.runSimulation();

        // Set the flag indicating that the simulation was run with priorities
        // Flag to track if the last simulation was done with priorities
        boolean lastSimulationWithPriorities = true;

        // Output a message indicating the simulation completion
        System.out.println("\nSimulation with priorities completed.");
    }
}
