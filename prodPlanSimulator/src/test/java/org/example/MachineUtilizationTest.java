package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link MachineUtilization} class.
 *
 * This class contains a set of JUnit tests to verify the behavior of the
 * {@link MachineUtilization} class. The tests cover functionalities such as
 * retrieving machine details, total time, utilization percentage, and operation type usage.
 */
public class MachineUtilizationTest {

    private MachineUtilization machineUtilization;

    /**
     * Sets up the test environment before each test execution.
     *
     * Initializes a new {@link MachineUtilization} instance with the ID "Machine1",
     * total time of 100, utilization percentage of 75.5, and operation type usage of 50.0.
     * This setup is performed before each test to ensure a consistent starting state.
     */
    @BeforeEach
    public void setup() {
        machineUtilization = new MachineUtilization("Machine1", 100, 75.5, 50.0);
    }

    /**
     * Tests the {@link MachineUtilization#getIdMachine()} method.
     *
     * Verifies that the machine ID is correctly returned as "Machine1".
     * This test ensures that the machine ID is properly stored and retrieved.
     */
    @Test
    public void testGetIdMachine() {
        assertEquals("Machine1", machineUtilization.getIdMachine(), "Machine ID should be 'Machine1'");
    }

    /**
     * Tests the {@link MachineUtilization#getTotalTime()} method.
     *
     * Verifies that the total time associated with the machine utilization is correctly returned as 100.
     * Ensures that the {@link MachineUtilization} class correctly handles the total time value.
     */
    @Test
    public void testGetTotalTime() {
        assertEquals(100, machineUtilization.getTotalTime(), "Total time should be 100");
    }

    /**
     * Tests the {@link MachineUtilization#getUtilizationPercentage()} method.
     *
     * Verifies that the utilization percentage is correctly returned as 75.5.
     * A delta value of 0.001 is used for comparison to account for floating-point precision.
     * Ensures that the class accurately represents the machine's utilization.
     */
    @Test
    public void testGetUtilizationPercentage() {
        assertEquals(75.5, machineUtilization.getUtilizationPercentage(), 0.001, "Utilization percentage should be 75.5");
    }

    /**
     * Tests the {@link MachineUtilization#getOperationTypeUsage()} method.
     *
     * Verifies that the operation type usage percentage is correctly returned as 50.0.
     * A delta value of 0.001 is used for comparison to ensure floating-point accuracy.
     * This test checks that the operation type usage is properly calculated and stored.
     */
    @Test
    public void testGetOperationTypeUsage() {
        assertEquals(50.0, machineUtilization.getOperationTypeUsage(), 0.001, "Operation type percentage should be 50.0");
    }
}
