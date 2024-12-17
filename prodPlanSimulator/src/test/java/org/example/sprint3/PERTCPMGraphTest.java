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

    @Test
    void buildGraph_withValidActivities() {
        graph.buildGraph(validActivities);

        // Assert vertices and edges
        assertEquals(6, graph.getGraph().vertices().size(), "Graph should include all activities plus START and END vertices.");
        assertEquals(6, graph.getGraph().edges().size(), "Graph should include all dependency edges plus START and END connections.");
    }

    @Test
    void buildGraph_withEmptyActivities() {
        graph.buildGraph(emptyActivities);

        // Assert empty graph
        assertEquals(2, graph.getGraph().vertices().size(), "Graph should only contain START and END vertices for an empty activity list.");
        assertEquals(1, graph.getGraph().edges().size(), "Graph should have Just START -> END for an empty activity list.");
    }

    @Test
    void validateNoCircularDependencies_withAcyclicGraph() {
        graph.buildGraph(validActivities);

        // Validate no exception thrown
        assertDoesNotThrow(graph::validateNoCircularDependencies, "Acyclic graph should not throw exceptions.");
    }

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

    @Test
    void topologicalSort_withValidGraph() {
        graph.buildGraph(validActivities);
        List<Activity> sorted = graph.topologicalSort();

        // Validate order (excluding START and END)
        assertEquals(6, sorted.size(), "Topological sort should include all activities, START, and END.");
        assertEquals(List.of("START", "A", "B", "C", "D", "END"), sorted.stream().map(Activity::getId).toList(), "Topological sort order should be correct.");
    }

    @Test
    void topologicalSort_withEmptyGraph() {
        graph.buildGraph(emptyActivities);
        List<Activity> sorted = graph.topologicalSort();

        // Validate empty result (just START and END)
        assertEquals(2, sorted.size(), "Topological sort should only return START and END for an empty graph.");
    }

    @Test
    void getTopologicalSortAsString_withValidGraph() {
        graph.buildGraph(validActivities);
        String result = graph.getTopologicalSortAsString();

        assertEquals("START -> A -> B -> C -> D -> END", result, "Topological sort string representation should include START and END.");
    }

    @Test
    void getTopologicalSortAsString_withEmptyGraph() {
        graph.buildGraph(emptyActivities);
        String result = graph.getTopologicalSortAsString();

        assertEquals("START -> END" , result, "Topological sort string representation should be START -> END for an empty graph.");
    }

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

    @Test
    void calculateEarliestAndLatestTimes_withEmptyGraph() {
        graph.buildGraph(emptyActivities);

        assertDoesNotThrow(graph::calculateEarliestAndLatestTimes, "Empty graphs should be handled gracefully.");
    }

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

    @Test
    void identifyCriticalPath_withValidGraph() {
        graph.buildGraph(validActivities);
        List<Activity> criticalPath = graph.identifyCriticalPath(validActivities);

        assertEquals(List.of("A", "C", "D"), criticalPath.stream().map(Activity::getId).toList(), "Critical path should only include activities with slack of 0.");
    }

    @Test
    void identifyBottleneckActivities_withValidGraph() {
        graph.buildGraph(validActivities);
        List<Activity> bottleneckActivities = graph.identifyBottleneckActivities();

        assertEquals(1, bottleneckActivities.size(), "There should be one bottleneck activity.");
        assertEquals("D", bottleneckActivities.get(0).getId(), "Activity D should be identified as a bottleneck.");
    }
}
