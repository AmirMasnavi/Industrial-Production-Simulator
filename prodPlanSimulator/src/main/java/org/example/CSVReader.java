package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading data from CSV files and converting them into lists of Java objects.
 * This class provides methods for reading two types of data: items and machines.
 * Each method reads a specified CSV file, converts its lines into corresponding Java objects,
 * and returns lists of those objects.
 * <p>
 * Note: The first line of the CSV file (presumably the header) is skipped during reading.
 * </p>
 */
public class CSVReader {

    /**
     * Reads a list of items from a CSV file.
     * This method opens the specified CSV file, skips the header line, and processes each subsequent line
     * to create an Item object. Each Item is instantiated by calling the {@link Article#fromCSV(String)} method.
     *
     * <p>
     * If an error occurs while parsing a line, an error message is printed to standard error, indicating
     * the problematic line and the reason for the error. If an IOException occurs while reading the file,
     * an error message is printed with details about the issue.
     * </p>
     *
     * @param filePath the path to the CSV file from which to read items
     * @return a List of Item objects created from the CSV file
     */
    public static List<Article> readArticlesFromCSV(String filePath) {
        List<Article> articles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            br.lines().forEach(line -> {
                try {
                    articles.add(Article.fromCSV(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing line: " + line + ". " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return articles;
    }

    /**
     * Reads a list of machines from a CSV file.
     * This method opens the specified CSV file, skips the header line, and processes each subsequent line
     * to create a Machine object. Each Machine is instantiated by calling the {@link Machine#fromCSV(String)} method.
     *
     * <p>
     * If an error occurs while parsing a line, an error message is printed to standard error, indicating
     * the problematic line and the reason for the error. If an IOException occurs while reading the file,
     * an error message is printed with details about the issue.
     * </p>
     *
     * @param filePath the path to the CSV file from which to read machines
     * @return a List of Machine objects created from the CSV file
     */
    public static List<Machine> readMachinesFromCSV(String filePath) {
        List<Machine> machines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            br.lines().forEach(line -> {
                try {
                    machines.add(Machine.fromCSV(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing line: " + line + ". " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return machines;
    }

    public static List<Item> readItemsFromCSV(String filePath) {
        List<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            br.lines().forEach(line -> {
                try {
                    String[] fields = line.split(";");
                    int id = Integer.parseInt(fields[0]);
                    String name = fields[1];
                    items.add(new Item(id, name));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error parsing line: " + line + ". " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return items;
    }

    public static List<Operation> readOperationsFromCSV(String filePath) {
        List<Operation> operations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            br.lines().forEach(line -> {
                try {
                    String[] fields = line.split(";");
                    int id = Integer.parseInt(fields[0]);
                    String name = fields[1];
                    operations.add(new Operation(id, name));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error parsing line: " + line + ". " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return operations;
    }


    public static BooDataResult readBooFromCSV(
            String filePath,
            Map<Integer, Integer> operationToItemMap // Pass this map to populate
    ) {
        Map<Integer, List<int[]>> booData = new HashMap<>();
        Map<Integer, Integer> itemQuantities = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            br.lines().forEach(line -> {
//                System.out.println("Processing line: " + line); // Debugging: Print current line
                try {
                    String[] fields = line.split(";");
                    int opId = Integer.parseInt(fields[0]);
                    int itemId = Integer.parseInt(fields[1]);
                    double itemQty = Double.parseDouble(fields[2].replace(",", "."));

                    // Store the item quantity
                    itemQuantities.put(itemId, (int) (itemQty * 1000));

                    // Map op_id to item_id for future lookup
                    operationToItemMap.put(opId, itemId);

                    List<int[]> subcomponents = new ArrayList<>();
                    boolean insideGroup = false;

                    for (int i = 3; i < fields.length; i++) {
                        String field = fields[i];

                        if (field.equals("(")) {
                            insideGroup = true;
//                            System.out.println("Entering group at index " + i); // Debugging
                            continue;
                        }
                        if (field.equals(")")) {
                            insideGroup = false;
//                            System.out.println("Exiting group at index " + i); // Debugging
                            continue;
                        }
                        if (field.isEmpty()) {
//                            System.out.println("Skipping empty field at index " + i); // Debugging
                            continue;
                        }

                        try {
                            int subItemId = Integer.parseInt(field);
                            double quantity = Double.parseDouble(fields[++i].replace(",", "."));
                            subcomponents.add(new int[]{subItemId, (int) (quantity * 1000)});
//                            System.out.println("Parsed subcomponent: " + subItemId + " with quantity " + quantity); // Debugging
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.err.println("Error parsing subcomponent at index " + i + ": " + e.getMessage());
                        }
                    }

                    booData.put(itemId, subcomponents);
//                    System.out.println("Parsed subcomponents for item " + itemId + ": " + subcomponents); // Debugging
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error parsing line: " + line + ". " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return new BooDataResult(booData, itemQuantities);
    }


}
