package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductImporter {

    public static void main(String[] args) {
        // Substitua pelo ProductID desejado
        String productID = "AS12945S22";

        // Conectar ao banco de dados Oracle
        try (Connection connection = DatabaseConnection.getConnection()) {

            // Verificar se a conexão foi estabelecida
            if (connection != null) {
                // Processar BillOfOperations
                exportBOOToCSV(connection, productID);

                // Processar BillOfMaterials
                // processBillOfMaterials(connection, productID);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void exportBOOToCSV(Connection connection, String productID) throws SQLException {
        // Consulta SQL para buscar os dados da BOO para o produto especificado
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

            // Criação do arquivo CSV
            String outputFileName = "BOO_" + productID + ".csv";
            try (FileWriter writer = new FileWriter(outputFileName)) {
                // Escreve cabeçalho do arquivo
                writer.append("Partnumber,OPID,QUANTITY,UNITY\n");

                // Processa os dados da consulta BOO_INPUT e escreve no arquivo
                boolean hasData = false;

                while (rsBOOInput.next()) {
                    hasData = true;
                    String partNumber = rsBOOInput.getString("Partnumber");
                    String opID = rsBOOInput.getString("OPID");
                    int quantity = rsBOOInput.getInt("QUANTITY");
                    String unity = rsBOOInput.getString("UNITY");

                    // Escreve os dados no CSV
                    writer.append(partNumber)
                            .append(",")
                            .append(opID)
                            .append(",")
                            .append(String.valueOf(quantity))
                            .append(",")
                            .append(unity)
                            .append("\n");
                }

                if (!hasData) {
                    System.out.println("Nenhum dado encontrado para o produto: " + productID);
                } else {
                    System.out.println("Arquivo CSV criado com sucesso: " + outputFileName);
                }
            } catch (IOException e) {
                System.err.println("Erro ao criar o arquivo CSV: " + e.getMessage());
            }
        }
    }

//    private static void processBillOfMaterials(Connection connection, String productID) throws SQLException {
//        String queryBOM = "SELECT * FROM PART WHERE ProductID = ?";
//        try (PreparedStatement stmtBOM = connection.prepareStatement(queryBOM)) {
//            stmtBOM.setString(1, productID);
//            ResultSet rsBOM = stmtBOM.executeQuery();
//
//            // Gerar o arquivo CSV para BillOfMaterials
//            try (FileWriter writer = new FileWriter("BillOfMaterials_" + productID + ".csv")) {
//                // Cabeçalho do CSV
//                writer.append("PartNumber,Description,Quantity\n");
//
//                while (rsBOM.next()) {
//                    String partNumber = rsBOM.getString("PartNumber");
//                    String description = rsBOM.getString("Description");
//                    int quantity = rsBOM.getInt("Quantity");
//
//                    // Escrever os dados no arquivo CSV
//                    writer.append(partNumber)
//                            .append(",")
//                            .append(description)
//                            .append(",")
//                            .append(String.valueOf(quantity))
//                            .append("\n");
//                }
//            } catch (IOException e) {
//                System.err.println("Erro ao escrever no arquivo CSV BillOfMaterials: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
}
