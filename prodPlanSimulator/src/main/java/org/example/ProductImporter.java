package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProductImporter {

    // Lista de produtos disponíveis
    private static final String[][] PRODUCTS = {
            {"AS12945T22", "La Belle 22 5l pot", "5l 22 cm aluminium and teflon non stick pot"},
            {"AS12945S22", "Pro 22 5l pot", "5l 22 cm stainless steel pot"},
            {"AS12945S20", "Pro 20 3l pot", "3l 20 cm stainless steel pot"},
            {"AS12945S17", "Pro 17 2l pot", "2l 17 cm stainless steel pot"},
            {"AS12945P17", "Pro 17 2l sauce pan", "2l 17 cm stainless steel souce pan"},
            {"AS12945S48", "Pro 17 lid", "17 cm stainless steel lid"},
            {"AS12945G48", "Pro Clear 17 lid", "17 cm glass lid"},
            {"AS12946S22", "Pro 22 5l pot bottom", "5l 22 cm stainless steel pot bottom"},
            {"AS12947S22", "Pro 22 lid", "22 cm stainless steel lid"},
            {"AS12946S20", "Pro 20 3l pot bottom", "3l 20 cm stainless steel pot bottom"},
            {"AS12947S20", "Pro 20 lid", "20 cm stainless steel lid"}
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Apresentar lista de produtos ao usuário
        System.out.println("Lista de produtos disponíveis:");
        for (int i = 0; i < PRODUCTS.length; i++) {
            System.out.printf("%d - %s (%s)\n", i + 1, PRODUCTS[i][1], PRODUCTS[i][2]);
        }

        // Perguntar ao usuário qual produto deseja
        System.out.print("Insira o número do produto que deseja exportar: ");
        int productIndex = scanner.nextInt() - 1;

        // Validar entrada do usuário
        if (productIndex < 0 || productIndex >= PRODUCTS.length) {
            System.out.println("Número inválido. Tente novamente.");
            return;
        }

        // Obter o ProductID escolhido
        String productId = PRODUCTS[productIndex][0];
        System.out.printf("Você escolheu: %s (%s)\n", PRODUCTS[productIndex][1], PRODUCTS[productIndex][2]);

        // Exportar BOO e BOM do produto escolhido
        exportDataToCSV(productId);
    }

    public static void exportDataToCSV(String productId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection == null) {
                System.out.println("Conexão com o banco de dados falhou!");
                return;
            }

            // Exportar dados do BOO
            exportBOOData(connection, productId);

            // Exportar dados do BOM
            exportBOMData(connection, productId);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportBOOData(Connection connection, String productId) throws SQLException, IOException {
        String queryBOO = "SELECT O.OPID " +
                "FROM Operation O " +
                "WHERE O.ProductID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(queryBOO)) {
            stmt.setString(1, productId);
            try (ResultSet rs = stmt.executeQuery();
                 FileWriter csvWriter = new FileWriter("BOO_" + productId + ".csv")) {

                // Escrever cabeçalho
                csvWriter.append("OperationID\n");

                // Escrever dados
                while (rs.next()) {
                    csvWriter.append(rs.getString("OPID")).append("\n");
                }

                System.out.println("Dados do BOO exportados para BOO_" + productId + ".csv");
            }
        }
    }

    private static void exportBOMData(Connection connection, String productId) throws SQLException, IOException {
        String queryBOM = "SELECT BI.PartNumber, SUM(BI.Quantity) AS Quantity " +
                "FROM BOO_INPUT BI " +
                "JOIN Operation O ON BI.OPID = O.OPID " +
                "WHERE O.ProductID = ? " +
                "GROUP BY BI.PartNumber";

        try (PreparedStatement stmt = connection.prepareStatement(queryBOM)) {
            stmt.setString(1, productId);
            try (ResultSet rs = stmt.executeQuery();
                 FileWriter csvWriter = new FileWriter("BOM_" + productId + ".csv")) {

                // Escrever cabeçalho
                csvWriter.append("PartNumber,Quantity\n");

                // Escrever dados
                while (rs.next()) {
                    csvWriter.append(rs.getString("PartNumber")).append(",")
                            .append(String.valueOf(rs.getInt("Quantity"))).append("\n");
                }

                System.out.println("Dados do BOM exportados para BOM_" + productId + ".csv");
            }
        }
    }
}
