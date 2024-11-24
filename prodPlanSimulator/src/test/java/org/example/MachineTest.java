package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Machine} class.
 * This class contains a set of JUnit tests to verify the behavior of the
 * {@link Machine} class. The tests cover functionalities such as retrieving
 * machine details, handling CSV input, and checking initial state values.
 */
public class MachineTest {

    private Machine machine;

    /**
     * Sets up the test environment before each test execution.
     * Initializes a new {@link Machine} instance with ID "Machine1", operation name
     * "Operation1", and time value of 5.
     */
    @BeforeEach
    public void setup() {
        machine = new Machine("Machine1", "Operation1", 5);
    }

    /**
     * Tests the {@link Machine#getIdMachine()} method.
     * Verifies that the machine ID is correctly returned as "Machine1".
     */
    @Test
    public void testGetIdMachine() {
        assertEquals("Machine1", machine.getIdMachine(), "Machine ID should be 'Machine1'");
    }

    /**
     * Tests the {@link Machine#getOperationName()} method.
     * Verifies that the operation name of the machine is correctly returned as "Operation1".
     */
    @Test
    public void testGetOperationName() {
        assertEquals("Operation1", machine.getOperationName(), "Operation name should be 'Operation1'");
    }

    /**
     * Tests the {@link Machine#getTime()} method.
     * Verifies that the time associated with the machine is correctly returned as 5.
     */
    @Test
    public void testGetTime() {
        assertEquals(5, machine.getTime(), "Time should be 5");
    }

    /**
     * Tests the {@link Machine#getAvailableTime()} method initially.
     * Verifies that the available time is initially set to 0 when a new {@link Machine}
     * instance is created.
     */
    @Test
    public void testGetAvailableTimeInitially() {
        assertEquals(0, machine.getAvailableTime(), "Initially, the available time should be 0");
    }

    /**
     * Tests the {@link Machine#fromCSV(String)} method with a valid CSV line.
     * Verifies that a {@link Machine} object is correctly created from a valid CSV
     * line, with the machine ID set to "Machine2", operation name to "Operation2",
     * and time to 10.
     */
    @Test
    public void testFromCSV() {
        String csvLine = "Machine2;Operation2;10";
        Machine newMachine = Machine.fromCSV(csvLine);

        assertEquals("Machine2", newMachine.getIdMachine());
        assertEquals("Operation2", newMachine.getOperationName());
        assertEquals(10, newMachine.getTime());
    }

    /**
     * Tests the {@link Machine#fromCSV(String)} method with an invalid CSV format.
     * Verifies that an {@link IllegalArgumentException} is thrown when attempting
     * to create a {@link Machine} from a CSV line that does not contain all required
     * fields (e.g., missing the time value).
     */
    @Test
    public void testFromCSV_InvalidFormat() {
        String invalidCsvLine = "Machine3;Operation3";
        assertThrows(IllegalArgumentException.class, () -> Machine.fromCSV(invalidCsvLine), "Expected IllegalArgumentException for invalid CSV line format");
    }

    /**
     * Tests the {@link Machine#toString()} method.
     * Verifies that the string representation of the {@link Machine} object matches
     * the expected format, including the machine ID, operation name, time, and the
     * initial available time value.
     */
    @Test
    public void testToString() {
        String expectedString = "Machine{idMachine='Machine1', operationName='Operation1', time=5}";
        assertEquals(expectedString, machine.toString(), "toString method output should match expected string");
    }
}
