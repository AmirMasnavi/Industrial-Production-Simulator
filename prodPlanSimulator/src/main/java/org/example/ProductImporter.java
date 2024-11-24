package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A utility class for importing and exporting product-related data from an Oracle database.
 * Currently, this class focuses on exporting Bill of Operations (BOO) data to a CSV file.
 * Future enhancements include processing the Bill of Materials (BOM).
 */
public class ProductImporter {

    /**
     * Main method for initiating the product import process. This connects to the database,
     * fetches the necessary data, and generates CSV files.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Replace with the desired ProductID
        String productID = "AS12945S22";

        // Connect to the Oracle database
        try (Connection connection = DatabaseConnection.getConnection()) {

            // Check if the connection was successfully established
            if (connection != null) {
                // Process Bill of Operations (BOO) and export to CSV
                exportBOOToCSV(connection, productID);

                // Placeholder for processing Bill of Materials (BOM) in the future
                // processBillOfMaterials(connection, productID);
            }

        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exports the Bill of Operations (BOO) data for a specified product to a CSV file.
     * The export includes associated inputs from the BOO_INPUT table.
     *
     * @param connection a valid database connection
     * @param productID  the identifier of the product whose BOO data will be exported
     * @throws SQLException if an error occurs during database interaction
     */
    public static void exportBOOToCSV(Connection connection, String productID) throws SQLException {
        // SQL query to fetch BOO data for the specified product
        String queryBOO = "SELECT * FROM BOO WHERE ProductID = ?";
        String queryBOOInput = "SELECT PARTNUMBER, OPID, QUANTITY, UNIT FROM BOO_INPUT WHERE OPID IN " +
                "(SELECT OPID FROM BOO WHERE ProductID = ?)";

        try (
                PreparedStatement stmtBOO = connection.prepareStatement(queryBOO);
                PreparedStatement stmtBOOInput = connection.prepareStatement(queryBOOInput)
        ) {
            stmtBOO.setString(1, productID);
            stmtBOOInput.setString(1, productID);

            ResultSet rsBOO = stmtBOO.executeQuery();
            ResultSet rsBOOInput = stmtBOOInput.executeQuery();

            // Create the output CSV file
            String outputFileName = "BOO_" + productID + ".csv";
            try (FileWriter writer = new FileWriter(outputFileName)) {
                // Write the CSV header
                writer.append("Partnumber,OPID,QUANTITY,UNITY\n");

                // Process BOO_INPUT data and write to the CSV file
                boolean hasData = false;

                while (rsBOOInput.next()) {
                    hasData = true;
                    String partNumber = rsBOOInput.getString("Partnumber");
                    String opID = rsBOOInput.getString("OPID");
                    int quantity = rsBOOInput.getInt("QUANTITY");
                    String unity = rsBOOInput.getString("UNITY");

                    // Write data to the CSV file
                    writer.append(partNumber)
                            .append(",")
                            .append(opID)
                            .append(",")
                            .append(String.valueOf(quantity))
                            .append(",")
                            .append(unity)
                            .append("\n");
                }

                // Provide feedback on the result
                if (!hasData) {
                    System.out.println("No data found for product: " + productID);
                } else {
                    System.out.println("CSV file successfully created: " + outputFileName);
                }
            } catch (IOException e) {
                System.err.println("Error creating the CSV file: " + e.getMessage());
            }
        }
    }

    // Placeholder for future implementation of Bill of Materials (BOM) processing
    // Uncomment and modify as needed
    /**
     * Processes the Bill of Materials (BOM) data for a specified product and exports it to a CSV file.
     *
     * @param connection a valid database connection
     * @param productID  the identifier of the product whose BOM data will be exported
     * @throws SQLException if an error occurs during database interaction
     */
//    private static void processBillOfMaterials(Connection connection, String productID) throws SQLException {
//        String queryBOM = "SELECT * FROM PART WHERE ProductID = ?";
//        try (PreparedStatement stmtBOM = connection.prepareStatement(queryBOM)) {
//            stmtBOM.setString(1, productID);
//            ResultSet rsBOM = stmtBOM.executeQuery();
//
//            // Generate the CSV file for BOM data
//            try (FileWriter writer = new FileWriter("BillOfMaterials_" + productID + ".csv")) {
//                // Write the CSV header
//                writer.append("PartNumber,Description,Quantity\n");
//
//                while (rsBOM.next()) {
//                    String partNumber = rsBOM.getString("PartNumber");
//                    String description = rsBOM.getString("Description");
//                    int quantity = rsBOM.getInt("Quantity");
//
//                    // Write data to the CSV file
//                    writer.append(partNumber)
//                            .append(",")
//                            .append(description)
//                            .append(",")
//                            .append(String.valueOf(quantity))
//                            .append("\n");
//                }
//            } catch (IOException e) {
//                System.err.println("Error writing the Bill of Materials CSV file: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
}
