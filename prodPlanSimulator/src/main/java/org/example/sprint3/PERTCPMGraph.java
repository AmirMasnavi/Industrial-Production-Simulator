package org.example.sprint3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Class representing a PERT/CPM (Program Evaluation Review Technique / Critical Path Method) graph.
 * This graph is used to model and analyze project activities, durations, dependencies, and critical paths.
 * The graph is built as a directed graph where vertices represent activities and edges represent dependencies.
 */
public class PERTCPMGraph {
    private final Graph<Activity, Integer> graph;

    /**
     * Constructor to initialize the PERT/CPM graph as a directed graph.
     */
    public PERTCPMGraph() {
        this.graph = new MapGraph<>(true); // Directed graph
    }

    /**
     * Builds the project graph by adding activities as vertices and creating edges for their dependencies.
     * Automatically includes "START" and "END" vertices to represent the start and end of the project.
     *
     * @param activities A list of activities to be included in the project graph.
     */
    public void buildGraph(List<Activity> activities) {
        Activity startActivity = null;
        Activity endActivity = null;

        // Check if START or END already exists
        boolean hasStart = graph.vertices().stream().anyMatch(activity -> activity.getId().equals("START"));
        boolean hasEnd = graph.vertices().stream().anyMatch(activity -> activity.getId().equals("END"));

        // Step 1: Create Start and End vertices
        if (!hasStart || !hasEnd) {
            startActivity = new Activity("START", "Project Start", 0, "week", 0, List.of());
            endActivity = new Activity("END", "Project End", 0, "week", 0, List.of());
        }

        if (!hasStart) {
            graph.addVertex(startActivity);
        }

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

        if (!hasEnd) {
            graph.addVertex(endActivity);
        }

        // Handle the case where the activity list is empty
        if (activities.isEmpty()) {
            graph.addEdge(startActivity, endActivity, 0); // Directly connect START to END
            return;
        }

        // Step 2: Connect Start to activities that have no predecessors
        for (Activity activity : activities) {
            if (activity.getDependencies().isEmpty() && startActivity != null) {
                graph.addEdge(startActivity, activity, 0); // No duration for Start to activity
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
            if (!activitiesWithSuccessors.contains(activity) && endActivity != null) {
                graph.addEdge(activity, endActivity, 0); // No duration for activity to End
            }
        }
    }

    /**
     * Validates the graph for circular dependencies using a depth-first search (DFS).
     * Throws an exception if a circular dependency is detected.
     *
     * @throws IllegalStateException if a circular dependency is found.
     */
    public void validateNoCircularDependencies() {
        // Title for validation process
        System.out.println("\n" + "═".repeat(50));
        System.out.println("🔍 \u001B[1mValidating Circular Dependencies\u001B[0m 🔍");
        System.out.println("═".repeat(50));

        // Sets to track visited nodes and recursion stack
        Set<Activity> visited = new HashSet<>();
        Set<Activity> recursionStack = new HashSet<>();

        for (Activity activity : graph.vertices()) {
            if (dfsDetectCycle(activity, visited, recursionStack)) {
                System.out.println("❌ \u001B[1;31mValidation failed:\u001B[0m Circular dependency detected!");
                throw new IllegalStateException("Circular dependency detected in the project graph.");
            }
        }

    }


    /**
     * Performs a topological sort of the project graph to determine an order of execution for activities.
     *
     * @return A list of activities in topological order.
     * @throws IllegalStateException if the graph contains a cycle.
     */
    public List<Activity> topologicalSort() {
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
     *
     * @return A string representing the topological order (e.g., "1 -> 2 -> 3").
     * @throws IllegalStateException if the graph contains a cycle.
     */
    public String getTopologicalSortAsString() {
        // Perform topological sort and store the result
        List<Activity> sortedActivities = topologicalSort();

        // Initialize a StringBuilder for building the output
        StringBuilder result = new StringBuilder();

        // Append each activity ID to the result with proper formatting
        for (int i = 0; i < sortedActivities.size(); i++) {
            result.append(sortedActivities.get(i).getId());
            if (i < sortedActivities.size() - 1) {
                result.append(" \u2192 "); // Use a right arrow for a clearer representation
            }
        }

        // Display the topological sort result
        System.out.println("\n" + "═".repeat(50));
        System.out.println("🔹 \u001B[1mTopological Sort Result\u001B[0m 🔹");
        System.out.println("═".repeat(50));
        System.out.println(result.toString());  // Print the topological sort as a string

        return result.toString();
    }


    /**
     * Calculates the earliest start (ES), earliest finish (EF), latest start (LS), and latest finish (LF)
     * times for all activities in the graph using forward and backward passes.
     */
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

    /**
     * Prints a formatted table of activity times, including:
     * Earliest Start (ES), Earliest Finish (EF), Latest Start (LS), Latest Finish (LF), and Slack.
     * Activities with IDs "START" and "END" are excluded from the output.
     */
    public void printActivityTimes() {
        // Print the table header with formatting
        System.out.println("\n" + "═".repeat(45));
        System.out.println("🔹 \u001B[1mActivity Times Summary\u001B[0m 🔹");
        System.out.println("═".repeat(45));
        System.out.println("ID\t|\tES\t|\tEF\t|\tLS\t|\tLF\t|\tSlack");

        // Iterate over the activities and print their times
        for (Activity activity : graph.vertices()) {
            // Skip the pseudo-activities "START" and "END"
            if (Objects.equals(activity.getId(), "START") || Objects.equals(activity.getId(), "END")) {
                continue;
            }

            // Print activity times with formatted output
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
     * Recursive helper method for detecting cycles in the graph using Depth-First Search (DFS).
     *
     * @param activity        The current activity being visited.
     * @param visited         A set of activities that have been fully processed.
     * @param recursionStack  A set of activities in the current DFS recursion stack.
     * @return True if a cycle is detected; false otherwise.
     */
    private boolean dfsDetectCycle(Activity activity, Set<Activity> visited, Set<Activity> recursionStack) {
        // If the activity is in the recursion stack, a cycle exists
        if (recursionStack.contains(activity)) {
            return true;
        }

        // If the activity is already visited and not in the stack, no cycle
        if (visited.contains(activity)) {
            return false;
        }

        // Mark the activity as visited and add it to the recursion stack
        visited.add(activity);
        recursionStack.add(activity);

        // Explore all adjacent vertices for cycles
        for (Activity neighbor : graph.adjVertices(activity)) {
            if (dfsDetectCycle(neighbor, visited, recursionStack)) {
                return true;
            }
        }

        // Remove the activity from the recursion stack after exploring all neighbors
        recursionStack.remove(activity);
        return false;
    }

    /**
     * Finds an activity in a list of activities by its ID.
     *
     * @param activities A list of activities to search.
     * @param id         The ID of the activity to find.
     * @return The activity with the given ID, or null if not found.
     */
    private Activity findActivityById(List<Activity> activities, String id) {
        return activities.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Retrieves the internal graph representation of activities and dependencies.
     *
     * @return The graph object representing the PERT/CPM network.
     */
    public Graph<Activity, Integer> getGraph() {
        return graph;
    }

    /**
     * Exports the activity schedule to a CSV file with detailed information for each activity.
     * The CSV format includes columns for activity ID, cost, duration, time metrics (ES, EF, LS, LF), slack, and dependencies.
     *
     * @param filePath The file path where the CSV will be saved.
     */
    public void exportScheduleToCsv(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write CSV header
            writer.write("act_id,cost,duration,es,ls,ef,lf,slack,prev_act_id1,...,prev_act_idN\n");

            // Write activity data
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
                    line.append(";").append(dependency); // Append each dependency
                }

                writer.write(line.append("\n").toString());
            }

            // Success message for successful export
            System.out.println("\n\u001B[32m✔️ Schedule exported successfully to:\u001B[0m " + filePath);

            // Error message for failure during export
            } catch (IOException e) {
            System.err.println("\n\u001B[31m❌ Error writing schedule to CSV:\u001B[0m " + e.getMessage());
             }

    }

    /**
     * Identifies and returns the critical path activities in the graph.
     * The critical path is composed of activities with zero slack.
     *
     * @return A list of activities on the critical path.
     */
    public List<Activity> identifyCriticalPath() {
        // Ensure time calculations are up-to-date
        calculateEarliestAndLatestTimes();

        List<Activity> criticalPath = new ArrayList<>();
        int maxDuration = 0;

        // Collect activities with zero slack
        for (Activity activity : graph.vertices()) {
            if (activity.getSlack() == 0) {
                criticalPath.add(activity);
            }
            maxDuration = Math.max(maxDuration, activity.getLatestFinish());
        }

        // Sort critical path activities by earliest start time
        criticalPath.sort(Comparator.comparingInt(Activity::getEarliestStart));

        // Print critical path activities with enhanced visual formatting
        System.out.println("\n" + "═".repeat(40));
        System.out.println("\uD83D\uDCCD  \u001B[1mCritical Path Activities\u001B[0m");
        System.out.println("═".repeat(40));
        System.out.println("\nID\t|\tES\t|\tEF\t|\tLS\t|\tLF\t|\tSL\t|\tDuration");

        // Displaying the activities in a structured and colorful format
        for (Activity activity : criticalPath) {
            System.out.printf("%s\u001B[0m\t|\t%2d\t|\t%2d\t|\t%2d\t|\t%2d\t|\t%2d\t|\t%d%n",
                    activity.getId(),
                    activity.getEarliestStart(),
                    activity.getEarliestFinish(),
                    activity.getLatestStart(),
                    activity.getLatestFinish(),
                    activity.getSlack(),
                    activity.getDuration());
        }

        System.out.println("\n\u001B[32mTotal project duration: " + maxDuration + " weeks\u001B[0m\n");

        return criticalPath;
    }


    /**
     * Identifies bottleneck activities in the graph.
     * Bottleneck activities are those with the highest number of dependencies.
     *
     * @return A list of bottleneck activities.
     */
    public List<Activity> identifyBottleneckActivities() {
        Map<Activity, Integer> dependencyCount = new HashMap<>();

        // Initialize dependency counts
        for (Activity activity : graph.vertices()) {
            dependencyCount.put(activity, 0);
        }

        // Count the dependencies for each activity
        for (Activity activity : graph.vertices()) {
            for (Activity neighbor : graph.adjVertices(activity)) {
                dependencyCount.put(neighbor, dependencyCount.get(neighbor) + 1);
            }
        }

        // Find the maximum number of dependencies
        int maxDependencies = Collections.max(dependencyCount.values());

        // Collect activities with the maximum dependency count
        List<Activity> bottleneckActivities = new ArrayList<>();
        for (Map.Entry<Activity, Integer> entry : dependencyCount.entrySet()) {
            if (entry.getValue() == maxDependencies) {
                bottleneckActivities.add(entry.getKey());
            }
        }

        return bottleneckActivities;
    }
}

