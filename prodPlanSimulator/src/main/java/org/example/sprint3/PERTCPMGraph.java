package org.example.sprint3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PERTCPMGraph {
    private final Graph<Activity, Integer> graph;

    public PERTCPMGraph() {
        this.graph = new MapGraph<>(true); // Directed graph
    }

    public void buildGraph(List<Activity> activities) {
        // Add vertices
        for (Activity activity : activities) {
            graph.addVertex(activity);
        }

        // Add edges based on dependencies
        for (Activity activity : activities) {
            for (int depId : activity.getDependencies()) {
                Activity depActivity = findActivityById(activities, depId);
                if (depActivity != null) {
                    graph.addEdge(depActivity, activity, activity.getDuration());
                }
            }
        }
    }

    /**
     * Validates the graph for circular dependencies using DFS.
     * @throws IllegalStateException if a circular dependency is found.
     */
    public void validateNoCircularDependencies() {
        Set<Activity> visited = new HashSet<>();
        Set<Activity> recursionStack = new HashSet<>();

        for (Activity activity : graph.vertices()) {
            if (dfsDetectCycle(activity, visited, recursionStack)) {
                throw new IllegalStateException("Circular dependency detected in the project graph.");
            }
        }
    }

    /**
     * Recursive helper method for DFS-based cycle detection.
     */
    private boolean dfsDetectCycle(Activity activity, Set<Activity> visited, Set<Activity> recursionStack) {
        // If the node is already in the recursion stack, there's a cycle
        if (recursionStack.contains(activity)) {
            return true;
        }

        // If the node is already visited and not in the stack, it's safe
        if (visited.contains(activity)) {
            return false;
        }

        // Mark the node as visited and add it to the recursion stack
        visited.add(activity);
        recursionStack.add(activity);

        // Check all outgoing edges for cycles
        for (Activity neighbor : graph.adjVertices(activity)) {
            if (dfsDetectCycle(neighbor, visited, recursionStack)) {
                return true;
            }
        }

        // Remove the node from the recursion stack after recursion completes
        recursionStack.remove(activity);
        return false;
    }

    private Activity findActivityById(List<Activity> activities, int id) {
        return activities.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    public Graph<Activity, Integer> getGraph() {
        return graph;
    }
}
