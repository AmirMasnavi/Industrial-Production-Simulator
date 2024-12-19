package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link Simulator} class.
 *
 * This class contains a set of JUnit tests to verify the behavior of the
 * {@link Simulator} class. The tests cover functionality related to the
 * initialization of the simulator and generating reports on machine utilization
 * and operation times.
 */
public class SimulatorTest {

    private Simulator simulator;
    private List<Article> articles;
    private List<Machine> machines;
    private DatabaseConnection dbConnection;

    /**
     * Sets up the test environment before each test execution.
     *
     * Initializes lists of {@link Article} and {@link Machine} objects and adds
     * instances to these lists. Two machines with different operation types and
     * two items with different priority levels are created. The {@link Simulator}
     * instance is then initialized with these lists.
     */
    @BeforeEach
    public void setup() {

        articles = new ArrayList<>();
        machines = new ArrayList<>();

        // Adding machines with specific operations and processing times
        machines.add(new Machine("M1", "Operation1", 5));
        machines.add(new Machine("M2", "Operation2", 10));

        // Adding items with different priorities and lists of operations
        articles.add(new Article("Item1", Article.Priority.NORMAL, List.of("Operation1", "Operation2")));
        articles.add(new Article("Item2", Article.Priority.HIGH, List.of("Operation2", "Operation1")));

        // Initializing the simulator with the items and machines
        simulator = new Simulator(articles, machines, dbConnection);
    }

    /**
     * Tests the {@link Simulator#presentMachineUtilizationReport()} method.
     *
     * Calls the method to present the machine utilization report.
     * This test ensures that the method runs without errors
     */
    @Test
    public void testPresentMachineUtilizationReport() {

        simulator.presentMachineUtilizationReport();

    }

    /**
     * Tests the {@link Simulator#presentOperationTimesReport()} method.
     *
     * Calls the method to present the operation times report.
     * This test ensures that the method executes successfully without any exceptions.
     */
    @Test
    public void testPresentOperationTimesReport() {

        simulator.presentOperationTimesReport();

    }


}
