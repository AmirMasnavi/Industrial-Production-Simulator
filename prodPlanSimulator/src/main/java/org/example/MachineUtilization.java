package org.example;

/**
 * Represents the utilization statistics of a machine, including its total operational time,
 * utilization percentage, and percentage of time spent on a specific type of operation.
 */
public class MachineUtilization {
    private final String idMachine;
    private final int totalTime;
    private final double utilizationPercentage;
    private final double operationTypePercentage;

    /**
     * Constructor to create a new MachineUtilization instance with the specified machine ID, total time,
     * utilization percentage, and operation type percentage.
     *
     * @param idMachine the unique identifier for the machine
     * @param totalTime the total time the machine has been operational
     * @param utilizationPercentage the percentage of time the machine was utilized
     * @param operationTypePercentage the percentage of time the machine was used for a specific operation type
     */
    public MachineUtilization(String idMachine, int totalTime, double utilizationPercentage, double operationTypePercentage) {
        this.idMachine = idMachine;
        this.totalTime = totalTime;
        this.utilizationPercentage = utilizationPercentage;
        this.operationTypePercentage = operationTypePercentage;
    }

    /**
     * Gets the unique identifier for the machine.
     *
     * @return the idMachine
     */
    public String getIdMachine() {
        return idMachine;
    }

    /**
     * Gets the total time the machine has been operational.
     *
     * @return the total operational time in appropriate units (e.g., minutes or hours)
     */
    public int getTotalTime() {
        return totalTime;
    }

    /**
     * Gets the utilization percentage of the machine.
     * This indicates the proportion of time the machine was actively utilized compared to its total available time.
     *
     * @return the utilization percentage as a double
     */
    public double getUtilizationPercentage() {
        return utilizationPercentage;
    }

    /**
     * Gets the percentage of time the machine was used for a specific operation type.
     *
     * @return the operation type usage percentage as a double
     */
    public double getOperationTypeUsage() {
        return operationTypePercentage;
    }
}
