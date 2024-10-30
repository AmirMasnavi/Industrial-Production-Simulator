package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an item with a unique identifier, a priority level, and a list of operations to be performed on it.
 * This class provides methods to manage and navigate through the operations associated with the item.
 * <p>
 * The priority of the item can be one of three levels: LOW, NORMAL, or HIGH, defined by the {@link Priority} enum.
 * The operations associated with the item are stored in a list and can be accessed sequentially.
 * </p>
 */
public class Item {
    private final String idItem;
    private final Priority priority;
    final List<String> operations;
    private int currentOperationIndex;

    /**
     * Enum representing the priority levels for an item.
     */
    public enum Priority {
        LOW, NORMAL, HIGH
    }

    /**
     * Constructor to create a new Item instance with a specified ID, priority, and list of operations.
     *
     * @param idItem the unique identifier for the item
     * @param priority the priority level of the item
     * @param operations a list of operations to be performed on the item
     */
    public Item(String idItem, Priority priority, List<String> operations) {
        this.idItem = idItem;
        this.priority = priority;
        this.operations = operations;
        this.currentOperationIndex = 0;
    }

    /**
     * Factory method to create an Item instance from a CSV line.
     * The CSV line should have the following format: idItem;priority;operation1;operation2;...
     * <p>
     * If the CSV line does not contain at least three fields, or if the priority is invalid,
     * an {@link IllegalArgumentException} will be thrown.
     * </p>
     *
     * @param csvLine a string representing a line from a CSV file
     * @return a new Item object created from the CSV data
     * @throws IllegalArgumentException if the CSV line format is invalid or if the priority is invalid
     */
    public static Item fromCSV(String csvLine) {
        String[] fields = csvLine.split(";");
        if (fields.length < 3) {
            throw new IllegalArgumentException("Invalid CSV line format");
        }

        String idItem = fields[0].trim();
        Priority priority;
        try {
            priority = Priority.valueOf(fields[1].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid priority value: " + fields[1]);
        }

        List<String> operations = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(fields, 2, fields.length)));
        operations.removeIf(String::isEmpty);

        return new Item(idItem, priority, operations);
    }

    /**
     * Retrieves the next operation in the list of operations.
     * If there are no more operations available, this method will return null.
     *
     * @return the next operation as a string, or null if no more operations are available
     */
    public String getNextOperation() {
        if (currentOperationIndex < operations.size()) {
            return operations.get(currentOperationIndex);
        }
        return null;
    }

    /**
     * Moves to the next operation in the list if one is available.
     *
     * @return true if the operation was successfully advanced; false if there are no more operations
     */
    public boolean moveToNextOperation() {
        if (currentOperationIndex < operations.size() - 1) {
            currentOperationIndex++;
            return true;
        }
        return false;
    }

    /**
     * Resets the operation index to the start of the operations list.
     */
    public void resetOperations() {
        currentOperationIndex = 0;
    }

    /**
     * Checks if there are remaining operations to perform.
     *
     * @return true if there are more operations available; false otherwise
     */
    public boolean hasMoreOperations() {
        return currentOperationIndex < operations.size();
    }

    /**
     * Checks if the item has finished all operations.
     *
     * @return true if all operations have been performed; false otherwise
     */
    public boolean isFinished() {
        return currentOperationIndex >= operations.size();
    }

    // Getters

    /**
     * Gets the unique identifier for the item.
     *
     * @return the idItem
     */
    public String getIdItem() {
        return idItem;
    }

    /**
     * Gets the priority level of the item.
     *
     * @return the priority of the item
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Returns a string representation of the Item object.
     *
     * @return a string describing the Item's id, priority, operations, and current operation index
     */
    @Override
    public String toString() {
        return "Item{" +
                "idItem='" + idItem + '\'' +
                ", priority=" + priority +
                ", operations=" + operations +
                '}';
    }
}
