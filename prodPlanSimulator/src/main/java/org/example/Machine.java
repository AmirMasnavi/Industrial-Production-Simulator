package org.example;

/**
 * Represents a machine that performs a specific operation for a given amount of time.
 * Each machine has a unique identifier, an operation name, and the time it takes to complete that operation.
 * The machine also tracks when it will be available to perform the next operation.
 */
public class Machine {
    private final String idMachine;
    private final String operationName;
    private final int time;  // Time the machine takes to perform an operation
    private int availableTime;  // When the machine will be available again

    /**
     * Constructor to create a new Machine instance with a specified ID, operation name, and operation time.
     *
     * @param idMachine the unique identifier for the machine
     * @param operationName the name of the operation performed by the machine
     * @param time the time taken by the machine to complete the operation
     */
    public Machine(String idMachine, String operationName, int time) {
        this.idMachine = idMachine;
        this.operationName = operationName;
        this.time = time;
        this.availableTime = 0;  // Initially available at time 0
    }

    /**
     * Factory method to create a Machine instance from a CSV line.
     * The CSV line should have the following format: idMachine;operationName;time
     * <p>
     * If the CSV line does not contain exactly three fields, or if the time value is invalid,
     * an {@link IllegalArgumentException} will be thrown.
     * </p>
     *
     * @param csvLine a string representing a line from a CSV file
     * @return a new Machine object created from the CSV data
     * @throws IllegalArgumentException if the CSV line format is invalid or if the time value is invalid
     */
    public static Machine fromCSV(String csvLine) {
        String[] fields = csvLine.split(";");
        if (fields.length != 3) {
            throw new IllegalArgumentException("Invalid CSV line format");
        }

        String idMachine = fields[0].trim();
        String operationName = fields[1].trim();
        int time;
        try {
            time = Integer.parseInt(fields[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time value: " + fields[2]);
        }

        return new Machine(idMachine, operationName, time);
    }

    // Getters for accessing machine attributes

    /**
     * Gets the unique identifier for the machine.
     *
     * @return the idMachine
     */
    public String getIdMachine() {
        return idMachine;
    }

    /**
     * Gets the name of the operation performed by the machine.
     *
     * @return the operationName
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * Gets the time taken by the machine to perform the operation.
     *
     * @return the time in units (e.g., minutes or seconds)
     */
    public int getTime() {
        return time;
    }

    /**
     * Gets the available time of the machine, indicating when it will be free to perform another operation.
     *
     * @return the availableTime in time units
     */
    public int getAvailableTime() {
        return availableTime;
    }

    /**
     * Sets the time until which the machine will be busy, effectively updating its available time.
     *
     * @param busyUntil the time at which the machine will be available again
     */
    public void setBusyUntil(int busyUntil) {
        this.availableTime = busyUntil;
    }

    /**
     * Returns a string representation of the Machine object.
     *
     * @return a string describing the Machine's id, operation name, operation time, and available time
     */
    @Override
    public String toString() {
        return "Machine{" +
                "idMachine='" + idMachine + '\'' +
                ", operationName='" + operationName + '\'' +
                ", time=" + time +
                '}';
    }
}
