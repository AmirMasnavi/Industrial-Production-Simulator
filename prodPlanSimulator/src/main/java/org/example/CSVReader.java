package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
     * to create an Item object. Each Item is instantiated by calling the {@link Item#fromCSV(String)} method.
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
    public static List<Item> readItemsFromCSV(String filePath) {
        List<Item> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            br.lines().forEach(line -> {
                try {
                    items.add(Item.fromCSV(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing line: " + line + ". " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return items;
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
}
