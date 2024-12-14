package org.example.sprint3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
                new Activity("A", "A", 3, "days", 100,  List.of()),  // No dependencies
                new Activity("B", "B", 2, "days", 200,  List.of("A")), // Depends on A
                new Activity("C", "C", 4, "days", 150,  List.of("A")), // Depends on A
                new Activity("D", "D", 2, "days", 300,  List.of("B", "C")) // Depends on B and C
        );

        emptyActivities = List.of(); // Empty activity list
    }

    @Test
    void buildGraph_withValidActivities() {
        graph.buildGraph(validActivities);

        // Assert vertices and edges
        assertEquals(4, graph.getGraph().vertices().size(), "Graph should contain all activities as vertices.");
        assertEquals(4, graph.getGraph().edges().size(), "Graph should contain all dependency edges.");
    }

    @Test
    void buildGraph_withEmptyActivities() {
        graph.buildGraph(emptyActivities);

        // Assert empty graph
        assertEquals(0, graph.getGraph().vertices().size(), "Graph should have no vertices for an empty activity list.");
        assertEquals(0, graph.getGraph().edges().size(), "Graph should have no edges for an empty activity list.");
    }

    @Test
    void validateNoCircularDependencies_withAcyclicGraph() {
        graph.buildGraph(validActivities);

        // Validate no exception thrown
        assertDoesNotThrow(graph::validateNoCircularDependencies, "Should not throw an exception for acyclic graphs.");
    }

    @Test
    void validateNoCircularDependencies_withCyclicGraph() {
        List<Activity> cyclicActivities = List.of(
                new Activity("A", "A", 3, "days", 100, List.of("C")), // A depends on C
                new Activity("B", "B", 2, "days", 200, List.of("A")), // B depends on A
                new Activity("C", "C", 4, "days", 150, List.of("B"))  // C depends on B (cycle)
        );
        graph.buildGraph(cyclicActivities);

        Exception exception = assertThrows(IllegalStateException.class, graph::validateNoCircularDependencies, "Should throw exception for cyclic graphs.");
        assertEquals("Circular dependency detected in the project graph.", exception.getMessage(), "Correct exception message for cycles.");
    }

    @Test
    void topologicalSort_withValidGraph() {
        graph.buildGraph(validActivities);
        List<Activity> sorted = graph.topologicalSort();

        // Validate order
        assertEquals(4, sorted.size(), "Topological sort should include all activities.");
        assertEquals(List.of("A", "B", "C", "D"), sorted.stream().map(Activity::getId).toList(), "Topological sort order should be correct.");
    }

    @Test
    void topologicalSort_withEmptyGraph() {
        graph.buildGraph(emptyActivities);
        List<Activity> sorted = graph.topologicalSort();

        // Validate empty result
        assertTrue(sorted.isEmpty(), "Topological sort should return an empty list for an empty graph.");
    }

    @Test
    void getTopologicalSortAsString_withValidGraph() {
        graph.buildGraph(validActivities);
        String result = graph.getTopologicalSortAsString();

        assertEquals("A -> B -> C -> D", result, "Topological sort string representation should be correct.");
    }

    @Test
    void getTopologicalSortAsString_withEmptyGraph() {
        graph.buildGraph(emptyActivities);
        String result = graph.getTopologicalSortAsString();

        assertEquals("", result, "Topological sort string representation should be empty for an empty graph.");
    }

    @Test
    void calculateEarliestAndLatestTimes_withValidGraph() {
        graph.buildGraph(validActivities);
        graph.calculateEarliestAndLatestTimes();

        Activity a = validActivities.get(0);
        Activity b = validActivities.get(1);
        Activity c = validActivities.get(2);
        Activity d = validActivities.get(3);

        // Assert calculated times
        assertEquals(0, a.getEarliestStart(), "A: Earliest start should be 0.");
        assertEquals(3, a.getEarliestFinish(), "A: Earliest finish should be 3.");
        assertEquals(3, b.getEarliestStart(), "B: Earliest start should be 3.");
        assertEquals(5, b.getEarliestFinish(), "B: Earliest finish should be 5.");
        assertEquals(3, c.getEarliestStart(), "C: Earliest start should be 3.");
        assertEquals(7, c.getEarliestFinish(), "C: Earliest finish should be 7.");
        assertEquals(7, d.getEarliestStart(), "D: Earliest start should be 7.");
        assertEquals(9, d.getEarliestFinish(), "D: Earliest finish should be 9.");
    }

    @Test
    void calculateEarliestAndLatestTimes_withEmptyGraph() {
        graph.buildGraph(emptyActivities);

        assertDoesNotThrow(graph::calculateEarliestAndLatestTimes, "Should handle empty graphs gracefully.");
    }

    @Test
    void printActivityTimes_withValidGraph() {
        graph.buildGraph(validActivities);
        graph.calculateEarliestAndLatestTimes();

        // Check for successful execution
        assertDoesNotThrow(graph::printActivityTimes, "Should not throw exceptions when printing activity times.");
    }

    @Test
    void getGraph_afterBuilding() {
        graph.buildGraph(validActivities);

        // Assert graph structure
        assertNotNull(graph.getGraph(), "Graph should not be null after building.");
        assertEquals(4, graph.getGraph().vertices().size(), "Graph should contain correct number of vertices.");
        assertEquals(4, graph.getGraph().edges().size(), "Graph should contain correct number of edges.");
    }
}
