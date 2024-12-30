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


