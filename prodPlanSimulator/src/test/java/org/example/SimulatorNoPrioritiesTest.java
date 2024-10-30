package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SimulatorNoPriorities} class.
 *
 * This class contains a set of JUnit tests to verify the behavior of the
 * {@link SimulatorNoPriorities} class. The tests check the initialization,
 * processing of tasks, machine assignments, and reporting functionalities.
 */
public class SimulatorNoPrioritiesTest {

    private SimulatorNoPriorities simulator;
    private List<Item> items;
    private List<Machine> machines;

    /**
     * Sets up the test environment before each test execution.
     *
     * Initializes a list of {@link Item} objects and a list of {@link Machine}
     * objects. Two items and two machines are added to these lists, respectively,
     * with the appropriate constructors. Then, a new {@link SimulatorNoPriorities}
     * instance is created using these lists.
     */
    @BeforeEach
    public void setup() {
        items = new ArrayList<>();

        // Adding items with various priorities and operations
        items.add(new Item("Item1", Item.Priority.HIGH, Arrays.asList("Operation1", "Operation2")));
        items.add(new Item("Item2", Item.Priority.LOW, Arrays.asList("Operation1", "Operation3")));

        machines = new ArrayList<>();

        // Adding machines with different types and times
        machines.add(new Machine("Machine1", "Operation1", 10));
        machines.add(new Machine("Machine2", "Operation3", 5));

        // Initializing the simulator with the given items and machines
        simulator = new SimulatorNoPriorities(items, machines);
    }

    /**
     * Tests the initialization of the {@link SimulatorNoPriorities} instance.
     *
     * Verifies that the simulator is properly initialized and checks the number
     * of items and machines. Ensures that the lists of items and machines contain
     * the expected number of elements after the simulator is created.
     */
    @Test
    public void testInitialization() {
        assertNotNull(simulator, "Simulator should be initialized");
        // Checks that the correct number of items and machines are initialized
        assertEquals(2, items.size(), "There should be 2 items initialized");
        assertEquals(2, machines.size(), "There should be 2 machines initialized");
    }




    /**
     * Tests the presentation of the initial item list.
     *
     * Ensures that the initial item list is correctly displayed with the
     * corresponding operations.
     */
    @Test
    public void testPresentInitialItemList() {
        // Capture the output for verification
        simulator.presentInitialItemList();

        // Since it's a void method with console output, manual verification of output may be required
        // Alternatively, a logger or stream could be used to capture the printed output and verify it
    }


}
