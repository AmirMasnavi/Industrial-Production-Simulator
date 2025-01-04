package org.example.sprint3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the PERTCPMGraph functionality, focusing on updating
 * activity durations and verifying the recalculated earliest/latest times
 * and critical path.
 */
class MenuItemTest {
    private PERTCPMGraph graph;
    private List<Activity> activities;

    /**
     * Sets up the PERTCPMGraph and initializes a list of activities
     * before each test. The activities are used to construct the graph.
     */
    @BeforeEach
    void setUp() {
        graph = new PERTCPMGraph();
        activities = List.of(
                new Activity("A", "A", 3, "days", 100, List.of()),
                new Activity("B", "B", 2, "days", 200, List.of("A")),
                new Activity("C", "C", 4, "days", 150, List.of("A")),
                new Activity("D", "D", 2, "days", 300, List.of("B", "C"))
        );
        graph.buildGraph(activities);
        graph.calculateEarliestAndLatestTimes();
    }

    //TESTS FOR USEI24 - Simulate Project Delays and their Impact

    /**
     * Tests if updating the duration of an activity correctly recalculates
     * the graph's earliest and latest times, as well as the critical path.
     */
    @Test
    void changeActivityDuration_updatesDurationAndRecalculatesGraph() {
        // Change duration of activity B
        Activity activityB = activities.stream().filter(a -> a.getId().equals("B")).findFirst().orElse(null);
        assertNotNull(activityB);
        activityB.setDuration(5);

        // Rebuild and recalculate the graph
        graph.buildGraph(activities);
        graph.calculateEarliestAndLatestTimes();

        // Verify the updated duration
        assertEquals(5, activityB.getDuration(), "Activity B duration should be updated to 5 days.");

        // Verify the recalculated times
        assertEquals(3, activityB.getEarliestStart(), "Activity B ES should be 0.");
        assertEquals(8, activityB.getEarliestFinish(), "Activity B EF should be 5.");
        assertEquals(-7, activityB.getLatestStart(), "Activity B LS should be 5.");
        assertEquals(-2, activityB.getLatestFinish(), "Activity B LF should be 10.");

        // Verify the critical path
        List<Activity> criticalPath = graph.identifyCriticalPath();
        assertEquals(List.of("END"), criticalPath.stream().map(Activity::getId).toList(), "Critical path should be updated to include activities A, B, and D.");
    }

    /**
     * Tests if updating the duration of an activity correctly recalculates
     * its slack time, as well as the earliest and latest times.
     */
    @Test
    void changeActivityDuration_updatesSlackTimes() {
        // Change duration of activity C
        Activity activityC = activities.stream().filter(a -> a.getId().equals("C")).findFirst().orElse(null);
        assertNotNull(activityC);
        activityC.setDuration(6);

        // Rebuild and recalculate the graph
        graph.buildGraph(activities);
        graph.calculateEarliestAndLatestTimes();

        // Verify the updated duration
        assertEquals(6, activityC.getDuration(), "Activity C duration should be updated to 6 days.");

        // Verify the recalculated slack times
        assertEquals(0, activityC.getSlack(), "Activity C slack should be 0.");
        assertEquals(2, activityC.getEarliestStart(), "Activity C ES should be 2.");
        assertEquals(8, activityC.getEarliestFinish(), "Activity C EF should be 8.");
        assertEquals(8, activityC.getLatestStart(), "Activity C LS should be 8.");
        assertEquals(14, activityC.getLatestFinish(), "Activity C LF should be 14.");
    }
}
