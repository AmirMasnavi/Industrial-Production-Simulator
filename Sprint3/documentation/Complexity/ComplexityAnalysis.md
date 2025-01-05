# Complexity

## Complexity Analysis:

### USEI17
> **buildGraph**

`````java
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


``````
***Complexity Analysis:***

**Building the graph:**
- Iterates through the list of activities to add vertices to the graph.
- For each activity, iterates through its dependencies to add edges.
- **Complexity:**
  - Adding vertices: O(A), where A is the number of activities.
  - Adding edges: O(D), where D is the number of dependencies.

**Total Complexity:**
- Let:
  - A = number of activities.
  - D = number of dependencies.
- **Overall complexity:** O(A + D).

> **findActivityById**

`````java
   private Activity findActivityById(List<Activity> activities, String id) {
  return activities.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
}
``````
***Complexity Analysis:***

**Finding an activity by ID:**
- Searches for an activity in the list by its ID.
- **Complexity:** Depends on the data structure used:
  - If a linear search is used: O(A), where A is the number of activities.
  - If a HashMap is used: O(1).

**Total Complexity:**
- Let:
  - A = number of activities.
- **Overall complexity:** O(A) (linear search) or O(1) (HashMap).


### USEI18
> **buildGraph**

`````java
     public void validateNoCircularDependencies() {
  Set<Activity> visited = new HashSet<>();
  Set<Activity> recursionStack = new HashSet<>();

  for (Activity activity : graph.vertices()) {
    if (dfsDetectCycle(activity, visited, recursionStack)) {
      throw new IllegalStateException("Circular dependency detected in the project graph.");
    }
  }
}
``````
***Complexity Analysis:***

**Detecting circular dependencies:**
- Uses a Depth-First Search (DFS) approach to detect cycles in the graph.
- **Complexity:**
  - Visits each vertex once: O(V).
  - Explores each edge during traversal: O(E).

**Tracking visited and recursion stack:**
- Maintains a visited set and a recursion stack to track the traversal state.
- Adding and checking elements in these sets: O(1) per operation.

**Total Complexity:**
- Let:
  - V = number of vertices (activities).
  - E = number of edges (dependencies).
- **Overall complexity:** O(V + E).

> **dfsDetectCycle**

`````java
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
``````
***Complexity Analysis:***

**Detecting cycles:**
- Implements Depth-First Search (DFS) to detect cycles in the graph.
- Visits each vertex once during the DFS traversal.
- For each vertex, checks all its outgoing edges.
- **Complexity:**
  - Visiting vertices: O(V), where V is the number of vertices.
  - Checking edges: O(E), where E is the number of edges.

**Total Complexity:**
- Let:
  - V = number of vertices.
  - E = number of edges.
- **Overall complexity:** O(V + E).

### USEI19
> **topologicalSort**

`````java
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
``````
***Complexity Analysis:***

**Initializing the in-degree map:**
- An in-degree map is created to track the number of incoming edges for each activity.
- **Complexity:** O(V), where V is the number of vertices (activities).

**Calculating in-degrees:**
- Iterates over all vertices and their neighbors (outgoing edges) to update the in-degree map.
- **Complexity:** O(E), where E is the number of edges (dependencies).

**Processing the in-degree zero queue:**
- Adds all vertices with in-degree zero to the queue.
- **Complexity:** O(V).

**Performing topological sorting:**
- Processes each vertex and its outgoing edges:
  - Each vertex is added to the result list: O(1).
  - For each edge, the in-degree of the target vertex is decremented: O(1) per edge.
  - If the in-degree of a vertex becomes zero, it is added to the queue: O(1).
- **Total complexity for this step:** O(V + E), as each vertex and edge is processed once.

**Total Complexity:**
- Let:
  - V = number of vertices (activities).
  - E = number of edges (dependencies).
- **Overall complexity:** O(V + E).

> **getTopologicalSortAsString**

`````java
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
``````
***Complexity Analysis:***

**Generating the output string:**
- Uses the result of `topologicalSort` to retrieve the list of activities.
- Iterates over the list to build the formatted string.
- **Complexity:**
  - Retrieving the sorted list: O(V), where V is the number of vertices.
  - Iterating and formatting the string: O(V).

**Total Complexity:**
- Let:
  - V = number of vertices.
- **Overall complexity:** O(V).


### USEI20
> **calculateEarliestAndLatestTimes**

`````java
public void calculateEarliestAndLatestTimes() {
  // Forward pass
  List<Activity> sortedActivities = topologicalSort();
  for (Activity activity : sortedActivities) {
    int es = 0;
    for (Edge<Activity, Integer> edge : graph.incomingEdges(activity)) {
      Activity predecessor = edge.getVOrig();
      es = Math.max(es, predecessor.getEarliestFinish());
    }
    activity.setEarliestStart(es);
    activity.setEarliestFinish(es + activity.getDuration());
  }

  // Backward pass
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

  // Calculate slack
  for (Activity activity : graph.vertices()) {
    activity.setSlack(activity.getLatestStart() - activity.getEarliestStart());
  }
}
``````
***Complexity Analysis:***

**Forward pass:**
- Iterates through the activities in topological order to calculate the earliest start (ES) and earliest finish (EF) times.
- For each activity, iterates through its predecessors to find the maximum EF.
- **Complexity:**
  - Iterating over activities: O(V).
  - Checking predecessors: O(E).

**Backward pass:**
- Iterates through the activities in reverse topological order to calculate the latest start (LS) and latest finish (LF) times.
- For each activity, iterates through its successors to find the minimum LS.
- **Complexity:**
  - Iterating over activities: O(V).
  - Checking successors: O(E).

**Calculating slack:**
- Iterates over all activities to calculate slack times (LF - EF or LS - ES).
- **Complexity:** O(V).

**Total Complexity:**
- Let:
  - V = number of vertices (activities).
  - E = number of edges (dependencies).
- **Overall complexity:** O(V + E).

> **printActivityTimes**

`````java

public void printActivityTimes() {
  System.out.println("ID\t|\tES\t|\tEF\t|\tLS\t|\tLF\t|\tSlack");
  for (Activity activity : graph.vertices()) {
    if (Objects.equals(activity.getId(), "START") || Objects.equals(activity.getId(), "END")) {
      continue; // Skip pseudo-activities "START" and "END"
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
``````
***Complexity Analysis:***

**Formatting and printing times:**
- Iterates over all activities in the graph to access their calculated values (ES, EF, LS, LF, Slack).
- Skips pseudo-activities ("START" and "END").
- Prints data for each valid activity.
- **Complexity:**
  - Iterating over vertices: O(V), where V is the number of vertices.
  - Printing data: O(V), as each vertex is processed once.

**Total Complexity:**
- Let:
  - V = number of vertices (activities).
- **Overall complexity:** O(V).

### USEI21
> **exportScheduleToCsv**

`````java
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

    System.out.println("\nSchedule exported successfully to: " + filePath);

  } catch (IOException e) {
    System.err.println("Error writing schedule to CSV: " + e.getMessage());
  }
}
``````
***Complexity Analysis:***

**Formatting the output:**
- Iterates over all activities to collect data (e.g., ID, cost, duration, ES, EF, LS, LF, dependencies).
- **Complexity:** O(V + D), where D is the total number of dependencies.

**Writing to a file:**
- Outputs formatted data to the CSV file.
- Writing each activity’s data: O(V).

**Total Complexity:**
- Let:
  - V = number of vertices (activities).
  - D = number of dependencies.
- **Overall complexity:** O(V + D).

### USEI22
> **identifyCriticalPath**

`````java
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

  // Print critical path activities
  System.out.println("\nCritical Path Activities:");
  System.out.println("ID\t|\tES\t|\tEF\t|\tLS\t|\tLF\t|\tSL\t|\tDuration");
  for (Activity activity : criticalPath) {
    System.out.printf("%s\t|\t%2d\t|\t%2d\t|\t%2d\t|\t%2d\t|\t%2d\t|\t%d%n",
            activity.getId(),
            activity.getEarliestStart(),
            activity.getEarliestFinish(),
            activity.getLatestStart(),
            activity.getLatestFinish(),
            activity.getSlack(),
            activity.getDuration());
  }

  System.out.println("\nTotal project duration: " + maxDuration + " weeks\n");

  return criticalPath;
}
``````
***Complexity Analysis:***

**Finding the critical path:**
- Uses the results from `calculateEarliestAndLatestTimes` to identify activities with zero slack.
- Iterates over all activities to check slack values and adds critical activities to the path.
- **Complexity:**
  - Checking slack: O(V).
  - Adding to the critical path: O(1) per activity.

**Sorting critical path:**
- Sorts critical path activities by their earliest start times.
- **Complexity:** O(C log C), where C is the number of activities on the critical path.

**Total Complexity:**
- Let:
  - V = number of vertices (activities).
  - C = number of activities on the critical path.
- **Overall complexity:** O(V + C log C).


### USEI23
> **identifyBottleneckActivities**

`````java
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

``````
***Complexity Analysis:***

**Counting dependencies:**
- Iterates over all vertices and their neighbors to count incoming edges for each activity.
- **Complexity:**
  - Iterating over vertices: O(V).
  - Iterating over edges: O(E).

**Finding maximum dependencies:**
- Iterates over all activities to find the maximum dependency count.
- **Complexity:** O(V).

**Collecting bottleneck activities:**
- Iterates over all activities to collect those with the maximum dependency count.
- **Complexity:** O(V).

**Total Complexity:**
- Let:
  - V = number of vertices (activities).
  - E = number of edges (dependencies).
- **Overall complexity:** O(V + E).



### USEI24
> **findActivityById**

```java  
public static Activity findActivityById(List<Activity> activities, String id) {  
    return activities.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);  
}  
```  

***Complexity Analysis:***

**Iterating through activities:**
- The `stream().filter()` operation iterates through the list of activities, applying the filter condition (`a.getId().equals(id)`) to each element.
- In the worst case, it checks all elements if the activity with the matching ID is not found earlier.

**Finding the first match:**
- The `findFirst()` operation stops once a match is found, or it continues until the end of the list if no match exists.

**Total Complexity:**
- Let:
  - n = number of activities in the list.
- **Overall complexity:** O(n).  


> **setDuration**

```java  
public void setDuration(int duration) {  
    this.duration = duration;  
}  
```  

***Complexity Analysis:***

**Setting a field value:**
- This method performs a single assignment operation, updating the `duration` field of the object.

**Total Complexity:**
- **Overall complexity:** O(1).  

> **changeActivityDuration**

```java  
private static void changeActivityDuration() {  
    Scanner scanner = new Scanner(System.in);  
    Activity activity = null;  

    while (activity == null) {  
        System.out.print("Enter the ID of the activity to change: ");  
        String activityId = scanner.next();  
        activity = findActivityById(activities, activityId);  

        if (activity == null) {  
            System.out.println("Activity ID not found. Please try again.");  
        }  
    }  

    System.out.print("Enter the new duration for the activity (in weeks): ");  
    int newDuration = scanner.nextInt();  

    activity.setDuration(newDuration);  
    System.out.println("\n\u001B[32m✔️ Activity duration updated successfully.\u001B[0m");  

    // Recalculate the graph  
    pertcpmGraph.calculateEarliestAndLatestTimes();  
}  
```  

***Complexity Analysis:***

**Finding the activity by ID:**
- Uses `findActivityById`, which has a complexity of O(n), where n is the number of activities in the list.
- In the worst case, the `while` loop repeatedly calls this method until a valid ID is entered. However, since the user input governs the loop, the complexity depends on the user's input, so it remains O(n) per call.

**Setting the new duration:**
- Calls `setDuration`, which has a complexity of O(1).

**Recalculating the graph:**
- Calls `calculateEarliestAndLatestTimes`, whose complexity depends on the graph's size and structure. Assuming the complexity of this method is O(V + E), where V is the number of vertices (activities) and E is the number of edges (dependencies).

**Total Complexity:**
- Let:
  - n = number of activities in the list.
  - V = number of vertices in the graph.
  - E = number of edges in the graph.
- **Overall complexity:** O(n + V + E).  
