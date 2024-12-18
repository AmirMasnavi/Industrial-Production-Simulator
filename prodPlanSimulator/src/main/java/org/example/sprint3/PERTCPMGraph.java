package org.example.sprint3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PERTCPMGraph {
    private final Graph<Activity, Integer> graph;

    public PERTCPMGraph() {
        this.graph = new MapGraph<>(true); // Directed graph
    }

    public void buildGraph(List<Activity> activities) {


        // Step 1: Create Start and End vertices
        Activity startActivity = new Activity("START", "Project Start", 0, "week", 0, List.of());
        Activity endActivity = new Activity("END", "Project End", 0, "week", 0, List.of());

        graph.addVertex(startActivity);
        // Add vertices for all activities
        for (Activity activity : activities) {
            graph.addVertex(activity);
        }

        // Add edges based on dependencies
        for (Activity activity : activities) {
            for (String depId : activity.getDependencies()) {
                Activity depActivity = findActivityById(activities, depId);
                if (depActivity != null) {
                    graph.addEdge(depActivity, activity, activity.getDuration());
                }
            }
        }

        graph.addVertex(endActivity);


        // Handle the case where the activity list is empty
        if (activities.isEmpty()) {
            graph.addEdge(startActivity, endActivity, 0);  // Directly connect START to END
            return;
        }


        // Step 2: Connect Start to activities that have no predecessors
        for (Activity activity : activities) {
            if (activity.getDependencies().isEmpty()) {
                graph.addEdge(startActivity, activity, 0);  // No duration for Start to activity
            }
        }

        // Step 3: Connect activities that have no successors to End
        HashSet<Activity> activitiesWithSuccessors = new HashSet<>();
        for (Activity activity : activities) {
            for (String depId : activity.getDependencies()) {
                Activity depActivity = findActivityById(activities, depId);
                if (depActivity != null) {
                    activitiesWithSuccessors.add(depActivity);
                }
            }
        }

        for (Activity activity : activities) {
            if (!activitiesWithSuccessors.contains(activity)) {
                graph.addEdge(activity, endActivity, 0);  // No duration for activity to End
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
     * Performs a topological sort of the project activities.
     * @return A list of activities in topological order.
     * @throws IllegalStateException if the graph contains a cycle.
     */
    public List<Activity> topologicalSort() {
        // Map to store in-degree of each vertex
        Map<Activity, Integer> inDegree = new HashMap<>();
        for (Activity activity : graph.vertices()) {
            inDegree.put(activity, 0);
        }

        // Calculate in-degrees
        for (Activity activity : graph.vertices()) {
            for (Activity neighbor : graph.adjVertices(activity)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // Queue for vertices with in-degree 0
        Queue<Activity> queue = new LinkedList<>();
        for (Map.Entry<Activity, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        // List to store the topological order
        List<Activity> topologicalOrder = new ArrayList<>();

        // Process vertices in the queue
        while (!queue.isEmpty()) {
            Activity current = queue.poll();
            topologicalOrder.add(current);

            // Reduce in-degree of neighbors
            for (Activity neighbor : graph.adjVertices(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Check if all vertices are processed
        if (topologicalOrder.size() != graph.vertices().size()) {
            throw new IllegalStateException("Graph contains a cycle! Topological sort not possible.");
        }

        return topologicalOrder;
    }

    /**
     * Returns the topological sort result as a formatted string of activity IDs.
     * @return A string representing the topological order (e.g., "1 -> 2 -> 3").
     * @throws IllegalStateException if the graph contains a cycle.
     */
    public String getTopologicalSortAsString() {
        List<Activity> sortedActivities = topologicalSort();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sortedActivities.size(); i++) {
            result.append(sortedActivities.get(i).getId());
            if (i < sortedActivities.size() - 1) {
                result.append(" -> ");
            }
        }
        return result.toString();
    }

    public void calculateEarliestAndLatestTimes() {
        List<Activity> sortedActivities = topologicalSort();
        if (sortedActivities == null) {
            throw new IllegalStateException("The graph contains cycles and is invalid.");
        }

        // Initialize times
        for (Activity activity : graph.vertices()) {
            activity.setEarliestStart(0);
            activity.setEarliestFinish(0);
            activity.setLatestStart(Integer.MAX_VALUE);
            activity.setLatestFinish(Integer.MAX_VALUE);
        }

        // Forward Pass: Calculate ES and EF
        for (Activity activity : sortedActivities) {
            int es = 0;
            for (Edge<Activity, Integer> edge : graph.incomingEdges(activity)) {
                Activity predecessor = edge.getVOrig();
                es = Math.max(es, predecessor.getEarliestFinish());
            }
            activity.setEarliestStart(es);
            activity.setEarliestFinish(es + activity.getDuration());
        }

        // Backward Pass: Calculate LS and LF
        ListIterator<Activity> iterator = sortedActivities.listIterator(sortedActivities.size());
        while (iterator.hasPrevious()) {
            Activity activity = iterator.previous();
            if (graph.outDegree(activity) == 0) { // End activities
                activity.setLatestFinish(activity.getEarliestFinish());
            }
            int lf = activity.getLatestFinish();
            for (Edge<Activity, Integer> edge : graph.outgoingEdges(activity)) {
                Activity successor = edge.getVDest();
                lf = Math.min(lf, successor.getLatestStart());
            }
            activity.setLatestFinish(lf);
            activity.setLatestStart(lf - activity.getDuration());
        }

        // Calculate Slack
        for (Activity activity : graph.vertices()) {
            int slack = activity.getLatestStart() - activity.getEarliestStart();
            activity.setSlack(slack);
        }
    }

    public void printActivityTimes() {

        System.out.println("ID\t|\tES\t|\tEF\t|\tLS\t|\tLF\t|\tSlack");
        for (Activity activity : graph.vertices()) {
            if (Objects.equals(activity.getId(), "START") || Objects.equals(activity.getId(), "END")) {
                continue;
            }
            System.out.printf("%s\t|\t%2d\t|\t%2d\t|\t%2d\t|\t%2d\t|\t%2d%n",
                    activity.getId(),
                    activity.getEarliestStart(),
                    activity.getEarliestFinish(),
                    activity.getLatestStart(),
                    activity.getLatestFinish(),
                    activity.getSlack());
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

    private Activity findActivityById(List<Activity> activities, String id) {
        return activities.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public Graph<Activity, Integer> getGraph() {
        return graph;
    }

    public void exportScheduleToCsv(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("act_id,cost,duration,es,ls,ef,lf,slack,prev_act_id1,...,prev_act_idN\n");

            for (Activity activity : graph.vertices()) {
                StringBuilder line = new StringBuilder();

                line.append(activity.getId()).append(";")
                        .append(activity.getCost()).append(";")
                        .append(activity.getDuration()).append(";")
                        .append(activity.getEarliestStart()).append(";")
                        .append(activity.getLatestStart()).append(";")
                        .append(activity.getEarliestFinish()).append(";")
                        .append(activity.getLatestFinish()).append(";")
                        .append(activity.getSlack());

                for (String dependency : activity.getDependencies()) {
                    line.append(";").append(dependency);
                }

                writer.write(line.append("\n").toString());
            }

            System.out.println("\nSchedule exported successfully to: " + filePath);

        } catch (IOException e) {
            System.err.println("Error writing schedule to CSV: " + e.getMessage());
        }
    }

    public List<Activity> identifyCriticalPath(List<Activity> activities) {
        // Calculate earliest and latest times
        calculateEarliestAndLatestTimes();

        List<Activity> criticalPath = new ArrayList<>();
        int maxDuration = 0;

        // Identify activities on the critical path
        for (Activity activity : activities) {
            if (activity.getSlack() == 0) {
                criticalPath.add(activity);
            }
            maxDuration = Math.max(maxDuration, activity.getLatestFinish());
        }

        // Print key metrics for activities on the critical path
        System.out.println("\nCritical Path Activities:");
        System.out.println("ID | ES | EF | LS | LF | Slack");
        for (Activity activity : criticalPath) {
            System.out.printf("%s | %2d | %2d | %2d | %2d | %2d%n",
                    activity.getId(),
                    activity.getEarliestStart(),
                    activity.getEarliestFinish(),
                    activity.getLatestStart(),
                    activity.getLatestFinish(),
                    activity.getSlack());
        }

        System.out.println("\nTotal project duration: " + maxDuration + " days");

        return criticalPath;
    }

    public List<Activity> identifyBottleneckActivities() {
        Map<Activity, Integer> dependencyCount = new HashMap<>();

        // Initialize dependency count for each activity
        for (Activity activity : graph.vertices()) {
            dependencyCount.put(activity, 0);
        }

        // Count the number of dependent activities for each activity
        for (Activity activity : graph.vertices()) {
            for (Activity neighbor : graph.adjVertices(activity)) {
                dependencyCount.put(neighbor, dependencyCount.get(neighbor) + 1);
            }
        }

        // Find the maximum dependency count
        int maxDependencies = Collections.max(dependencyCount.values());

        // Identify activities with the maximum dependency count
        List<Activity> bottleneckActivities = new ArrayList<>();
        for (Map.Entry<Activity, Integer> entry : dependencyCount.entrySet()) {
            if (entry.getValue() == maxDependencies) {
                bottleneckActivities.add(entry.getKey());
            }
        }

        return bottleneckActivities;
    }

}
