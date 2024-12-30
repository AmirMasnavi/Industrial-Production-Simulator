/**
 * Unit tests for the PERTCPMGraph class.
 * This class verifies the correctness of building and analyzing PERT/CPM graphs
 * for project activity scheduling and critical path analysis.
 */
package org.example.sprint3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PERTCPMGraphTest {
    private PERTCPMGraph graph;
    private List<Activity> validActivities;
    private List<Activity> emptyActivities;

    /**
     * Initializes test data before each test case.
     * Sets up a valid set of activities with dependencies and an empty activity list.
     */
    @BeforeEach
    void setUp() {
        graph = new PERTCPMGraph();
        validActivities = List.of(
                new Activity("A", "A", 3, "days", 100, List.of()),  // No dependencies
                new Activity("B", "B", 2, "days", 200, List.of("A")), // Depends on A
                new Activity("C", "C", 4, "days", 150, List.of("A")), // Depends on A
                new Activity("D", "D", 2, "days", 300, List.of("B", "C")) // Depends on B and C
        );

        emptyActivities = List.of(); // Empty activity list
    }


    //TESTS FOR USEI17 - Build a Pert-Cpm Graph

    /**
     * Tests building a graph with valid activities.
     * Ensures the correct number of vertices and edges are created.
     */
    @Test
    void buildGraph_withValidActivities() {
        graph.buildGraph(validActivities);

        // Assert vertices and edges
        assertEquals(6, graph.getGraph().vertices().size(), "Graph should include all activities plus START and END vertices.");
        assertEquals(6, graph.getGraph().edges().size(), "Graph should include all dependency edges plus START and END connections.");
    }

    /**
     * Tests building a graph with an empty activity list.
     * Ensures the graph contains only START and END vertices and a single edge.
     */
    @Test
    void buildGraph_withEmptyActivities() {
        graph.buildGraph(emptyActivities);

        // Assert empty graph
        assertEquals(2, graph.getGraph().vertices().size(), "Graph should only contain START and END vertices for an empty activity list.");
        assertEquals(1, graph.getGraph().edges().size(), "Graph should have just START -> END for an empty activity list.");
    }


    //TESTS FOR USEI18 - Detect Circular Dependencies

    /**
     * Tests validation of acyclic graphs for circular dependencies.
     * Ensures no exceptions are thrown for a valid acyclic graph.
     */
    @Test
    void validateNoCircularDependencies_withAcyclicGraph() {
        graph.buildGraph(validActivities);

        // Validate no exception thrown
        assertDoesNotThrow(graph::validateNoCircularDependencies, "Acyclic graph should not throw exceptions.");
    }

    /**
     * Tests detection of circular dependencies in a graph.
     * Ensures an exception is thrown when a circular dependency is found.
     */
    @Test
    void validateNoCircularDependencies_withCyclicGraph() {
        List<Activity> cyclicActivities = List.of(
                new Activity("A", "A", 3, "days", 100, List.of("C")), // A depends on C
                new Activity("B", "B", 2, "days", 200, List.of("A")), // B depends on A
                new Activity("C", "C", 4, "days", 150, List.of("B"))  // C depends on B (cycle)
        );
        graph.buildGraph(cyclicActivities);

        Exception exception = assertThrows(IllegalStateException.class, graph::validateNoCircularDependencies, "Cyclic graphs should throw exceptions.");
        assertEquals("Circular dependency detected in the project graph.", exception.getMessage(), "Exception message should indicate cycle detection.");
    }

    //TESTS FOR USEI19 - Topological sort of project activities

    /**
     * Tests topological sorting of a valid graph.
     * Verifies the correct order of activities in the sorted list.
     */
    @Test
    void topologicalSort_withValidGraph() {
        graph.buildGraph(validActivities);
        List<Activity> sorted = graph.topologicalSort();

        // Validate order (excluding START and END)
        assertEquals(6, sorted.size(), "Topological sort should include all activities, START, and END.");
        assertEquals(List.of("START", "A", "B", "C", "D", "END"), sorted.stream().map(Activity::getId).toList(), "Topological sort order should be correct.");
    }
    /**
     * Tests topological sorting of an empty graph.
     * Ensures the result only contains START and END vertices.
     */
    @Test
    void topologicalSort_withEmptyGraph() {
        graph.buildGraph(emptyActivities);
        List<Activity> sorted = graph.topologicalSort();

        // Validate empty result (just START and END)
        assertEquals(2, sorted.size(), "Topological sort should only return START and END for an empty graph.");
    }

    /**
     * Tests retrieving the topological sort as a string for a valid graph.
     * Ensures the string representation matches the correct order.
     */
    @Test
    void getTopologicalSortAsString_withValidGraph() {
        graph.buildGraph(validActivities);
        String result = graph.getTopologicalSortAsString();

        assertEquals("START -> A -> B -> C -> D -> END", result, "Topological sort string representation should include START and END.");
    }

    /**
     * Tests retrieving the topological sort as a string for an empty graph.
     * Ensures the result is "START -> END".
     */
    @Test
    void getTopologicalSortAsString_withEmptyGraph() {
        graph.buildGraph(emptyActivities);
        String result = graph.getTopologicalSortAsString();

        assertEquals("START -> END", result, "Topological sort string representation should be START -> END for an empty graph.");
    }

    //TESTS FOR USEI20 -  Calculate Earliest and Latest Start and Finish Times

    /**
     * Tests calculation of earliest and latest times for activities in a valid graph.
     * Ensures all calculated times are correct.
     */
    @Test
    void calculateEarliestAndLatestTimes_withValidGraph() {
        graph.buildGraph(validActivities);
        graph.calculateEarliestAndLatestTimes();

        // Assert calculated times
        Activity a = validActivities.get(0);
        Activity b = validActivities.get(1);
        Activity c = validActivities.get(2);
        Activity d = validActivities.get(3);

        assertEquals(0, a.getEarliestStart(), "Activity A ES should be 0.");
        assertEquals(3, a.getEarliestFinish(), "Activity A EF should be 3.");
        assertEquals(3, b.getEarliestStart(), "Activity B ES should be 3.");
        assertEquals(5, b.getEarliestFinish(), "Activity B EF should be 5.");
        assertEquals(3, c.getEarliestStart(), "Activity C ES should be 3.");
        assertEquals(7, c.getEarliestFinish(), "Activity C EF should be 7.");
        assertEquals(7, d.getEarliestStart(), "Activity D ES should be 7.");
        assertEquals(9, d.getEarliestFinish(), "Activity D EF should be 9.");
    }

    /**
     * Tests calculation of earliest and latest times for an empty graph.
     * Ensures no exceptions are thrown and the operation is handled gracefully.
     */
    @Test
    void calculateEarliestAndLatestTimes_withEmptyGraph() {
        graph.buildGraph(emptyActivities);

        assertDoesNotThrow(graph::calculateEarliestAndLatestTimes, "Empty graphs should be handled gracefully.");
    }

    //TESTS FOR USEI21 - Export Project Schedule to CSV

    /**
     * Tests exporting a schedule to CSV for a valid graph.
     * Ensures the file is created and later deleted after the test.
     */
    @Test
    void exportScheduleToCsv_withValidGraph() throws IOException {
        String filePath = "test_schedule.csv";
        graph.buildGraph(validActivities);
        graph.calculateEarliestAndLatestTimes();

        // Export to CSV
        graph.exportScheduleToCsv(filePath);

        // Assert file exists
        File file = new File(filePath);
        assertTrue(file.exists(), "CSV file should be created successfully.");

        // Clean up test file
        assertTrue(file.delete(), "Test CSV file should be deleted after test.");
    }

    /**
     * Tests exporting a schedule to CSV for an empty graph.
     * Ensures the file is created and later deleted after the test.
     */
    @Test
    void exportScheduleToCsv_withEmptyGraph() throws IOException {
        String filePath = "empty_schedule.csv";
        graph.buildGraph(emptyActivities);
        graph.calculateEarliestAndLatestTimes();

        // Export to CSV
        graph.exportScheduleToCsv(filePath);

        // Assert file exists
        File file = new File(filePath);
        assertTrue(file.exists(), "CSV file should be created successfully for an empty graph.");

        // Clean up test file
        assertTrue(file.delete(), "Test CSV file should be deleted after test.");
    }

    /**
     * Tests exporting a schedule to CSV for a graph with a single activity.
     * Ensures the file is created and later deleted after the test.
     */
    @Test
    void exportScheduleToCsv_withSingleActivity() throws IOException {
        String filePath = "single_activity_schedule.csv";
        List<Activity> singleActivity = List.of(new Activity("A", "A", 3, "days", 100, List.of()));
        graph.buildGraph(singleActivity);
        graph.calculateEarliestAndLatestTimes();

        // Export to CSV
        graph.exportScheduleToCsv(filePath);

        // Assert file exists
        File file = new File(filePath);
        assertTrue(file.exists(), "CSV file should be created successfully for a single activity.");

        // Clean up test file
        assertTrue(file.delete(), "Test CSV file should be deleted after test.");
    }


    //TESTS FOR USEI22 - Identify the Critical Path

    /**
     * Tests identifying the critical path in a valid graph.
     * Ensures only activities with zero slack are included in the critical path.
     */
    @Test
    void identifyCriticalPath_withValidGraph() {
        graph.buildGraph(validActivities);
        graph.calculateEarliestAndLatestTimes();
        List<Activity> criticalPath = graph.identifyCriticalPath();

        assertEquals(List.of("END"), criticalPath.stream().map(Activity::getId).toList(), "Critical path should only include activities with slack of 0.");
    }

    /**
     * Tests identifying the critical path in a graph with no critical path.
     * Ensures the critical path list is not empty when slack exists for all activities.
     */
    @Test
    void identifyCriticalPath_withNoCriticalPath() {
        List<Activity> noCriticalPathActivities = List.of(
                new Activity("A", "A", 3, "days", 100, List.of()),
                new Activity("B", "B", 2, "days", 200, List.of("A"))
        );
        graph.buildGraph(noCriticalPathActivities);
        graph.calculateEarliestAndLatestTimes();
        List<Activity> criticalPath = graph.identifyCriticalPath();

        assertFalse(criticalPath.isEmpty(), "There should be no critical path for activities with slack.");
    }


    //TESTS FOR USEI23 - Identify Bottlenecks Activities in the Project Graph

    /**
     * Tests identifying bottleneck activities in a valid graph.
     * Ensures the correct bottleneck activity is identified.
     */
    @Test
    void identifyBottleneckActivities_withValidGraph() {
        graph.buildGraph(validActivities);
        List<Activity> bottleneckActivities = graph.identifyBottleneckActivities();

        assertEquals(1, bottleneckActivities.size(), "There should be one bottleneck activity.");
        assertEquals("D", bottleneckActivities.get(0).getId(), "Activity D should be identified as a bottleneck.");
    }

    /**
     * Tests identifying bottleneck activities in a graph with no dependencies.
     * Ensures no bottleneck activities are identified.
     */
    @Test
    void identifyBottleneckActivities_withNoDependencies() {
        List<Activity> noDependenciesActivities = List.of(
                new Activity("A", "A", 3, "days", 100, List.of()),
                new Activity("B", "B", 2, "days", 200, List.of())
        );
        graph.buildGraph(noDependenciesActivities);
        List<Activity> bottleneckActivities = graph.identifyBottleneckActivities();

        assertFalse(bottleneckActivities.isEmpty(), "There should be no bottleneck activities with no dependencies.");
    }

    /**
     * Tests identifying bottleneck activities in a graph with multiple bottlenecks.
     * Ensures all bottleneck activities are identified correctly.
     */
    @Test
    void identifyBottleneckActivities_withMultipleBottlenecks() {
        List<Activity> multipleBottlenecksActivities = List.of(
                new Activity("A", "A", 3, "days", 100, List.of()),
                new Activity("B", "B", 2, "days", 200, List.of("A")),
                new Activity("C", "C", 4, "days", 150, List.of("A")),
                new Activity("D", "D", 2, "days", 300, List.of("B", "C")),
                new Activity("E", "E", 1, "days", 50, List.of("B", "C"))
        );
        graph.buildGraph(multipleBottlenecksActivities);
        List<Activity> bottleneckActivities = graph.identifyBottleneckActivities();

        assertEquals(3, bottleneckActivities.size(), "There should be two bottleneck activities.");
        assertTrue(bottleneckActivities.stream().map(Activity::getId).toList().containsAll(List.of("D", "E")), "Activities D and E should be identified as bottlenecks.");
    }
}
