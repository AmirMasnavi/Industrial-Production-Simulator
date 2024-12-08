package org.example;

import org.example.sprint3.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for reading data from CSV files and converting them into structured Java objects.
 * <p>
 * This class includes various methods to process CSV files representing different types of data,
 * such as articles, machines, items, operations, and Bill of Operations (BOO) data.
 * It provides flexible functionality for parsing and handling errors during file reading.
 */
public class CSVReader {

    /**
     * Reads a list of articles from a CSV file.
     * <p>
     * Each line is processed using the {@code Article.fromCSV(String)} method to instantiate an {@code Article} object.
     * Skips the header line in the file.
     *
     * @param filePath the path to the CSV file containing article data
     * @return a list of {@code Article} objects
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
     * <p>
     * Each line is processed using the {@code Machine.fromCSV(String)} method to instantiate a {@code Machine} object.
     * Skips the header line in the file.
     *
     * @param filePath the path to the CSV file containing machine data
     * @return a list of {@code Machine} objects
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

    /**
     * Reads a list of items from a CSV file.
     * <p>
     * Processes each line to create an {@code Item} object with ID and name fields.
     * Skips the header line in the file.
     *
     * @param filePath the path to the CSV file containing item data
     * @return a list of {@code Item} objects
     */
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

    /**
     * Reads a list of operations from a CSV file.
     * <p>
     * Processes each line to create an {@code Operation} object with ID and name fields.
     * Skips the header line in the file.
     *
     * @param filePath the path to the CSV file containing operation data
     * @return a list of {@code Operation} objects
     */
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

    /**
     * Reads Bill of Operations (BOO) data from a CSV file.
     * <p>
     * Processes lines to populate the {@code booData} map with operation-to-suboperation relationships
     * and the {@code itemQuantities} map with item quantities. Additionally, updates the provided
     * {@code operationToItemMap} for operation-to-item mappings.
     * Skips the header line in the file.
     *
     * @param filePath           the path to the CSV file containing BOO data
     * @param operationToItemMap a map to populate with operation-to-item mappings
     * @return a {@code BooDataResult} object containing parsed BOO data and item quantities
     */
    public static BooDataResult readBooFromCSV(
            String filePath,
            Map<Integer, Integer> operationToItemMap
    ) {
        Map<Integer, Map<Integer, Double>> booData = new HashMap<>();
        Map<Integer, Double> itemQuantities = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            br.lines().forEach(line -> {
                try {
                    String[] fields = line.split(";");
                    int opId = Integer.parseInt(fields[0]);
                    int itemId = Integer.parseInt(fields[1]);
                    double itemQty = Double.parseDouble(fields[2].replace(",", "."));

                    // Store item quantity and map operation to item
                    itemQuantities.put(itemId, itemQty);
                    operationToItemMap.put(opId, itemId);

                    // Parse subcomponents for the operation
                    Map<Integer, Double> subcomponents = new HashMap<>();
                    for (int i = 3; i < fields.length; i++) {
                        String field = fields[i];
                        if (!field.isEmpty() && !field.equals("(") && !field.equals(")")) {
                            int subItemId = Integer.parseInt(field);
                            double quantity = Double.parseDouble(fields[++i].replace(",", "."));
                            subcomponents.put(subItemId, quantity);
                            if (subItemId > 999) {
                                itemQuantities.put(subItemId, quantity);
                            }
                        }
                    }
                    booData.put(itemId, subcomponents);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error parsing line: " + line + ". " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return new BooDataResult(booData, itemQuantities);
    }

    public static List<Activity> readActivitiesFromCsv(String filePath) {
        List<Activity> activities = new ArrayList<>();
        HashMap<Integer, Activity> activityMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip header
            line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Invalid CSV format");
                }

                int id = Integer.parseInt(parts[0]);
                String description = parts[1];
                int duration = Integer.parseInt(parts[2]);
                String durationUnit = parts[3];
                double cost = Double.parseDouble(parts[4]);
                String costUnit = parts[5];

                List<Integer> dependencies = new ArrayList<>();
                for (int i = 6; i < parts.length; i++) {
                    dependencies.add(Integer.parseInt(parts[i]));
                }

                Activity activity = new Activity(id, description, duration, durationUnit, cost, costUnit, dependencies);
                activities.add(activity);
                activityMap.put(id, activity);
            }

            // Validate dependencies
            for (Activity activity : activities) {
                for (int dependency : activity.getDependencies()) {
                    if (!activityMap.containsKey(dependency)) {
                        throw new IllegalArgumentException("Invalid dependency for activity " + activity.getId());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading CSV file", e);
        }

        return activities;
    }
}
