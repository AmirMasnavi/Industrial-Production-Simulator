# Complexity

## Complexity Analysis:
# test
### USEI01
> Item.fromCsv

`````java
public static Item fromCSV(String csvLine) {
  String[] fields = csvLine.split(";");
  if (fields.length < 3) {
    throw new IllegalArgumentException("Invalid CSV line format");
  }

  String idItem = fields[0].trim();
  Priority priority;
  try {
    priority = Priority.valueOf(fields[1].trim().toUpperCase());
  } catch (IllegalArgumentException e) {
    throw new IllegalArgumentException("Invalid priority value: " + fields[1]);
  }

  List<String> operations = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(fields, 2, fields.length)));
  operations.removeIf(String::isEmpty);

  return new Item(idItem, priority, operations);
}
``````

**Complexity Analysis:**

* **Splitting the CSV Line:** csvLine.split(";") splits the input line by ; into an array of fields. If the input string has n characters, this operation has a complexity of O(n).
* **Field Validation:** fields.length < 3 checks the length of the array, which is O(1).
* **Passing the priority field:** Priority.valueOf(fields[1].trim().toUpperCase()) processes the priority field by trimming whitespace, converting it to uppercase, and then checking if it matches an enum value. trim() and toUpperCase() have complexities of O(m) (where m is the length of fields[1]), which is small and typically considered O(1) in the context of the method. Converting the string to an enum type has O(1) complexity, given a small, fixed set of priorities.
* **Parsing and filtering the operations list:** Arrays.copyOfRange(fields, 2, fields.length) creates a subarray of operations. If there are p remaining fields (each representing an operation), this copying operation has a complexity of O(p). new ArrayList<>(...) wraps the array in an ArrayList, which is O(p). operations.removeIf(String::isEmpty) iterates through the list and removes empty strings, resulting in O(p) complexity.
* **Returning the Item:** Constructing and returning a new Item instance is O(1).

**Total Complexity:** The overall complexity is dominated by the initial split and processing of fields, which is O(n + p), where n is the number of characters in the csvLine string and p is the number of operations fields.

> Machine.fromCsv

`````java
public static Machine fromCSV(String csvLine) {
  String[] fields = csvLine.split(";");
  if (fields.length != 3) {
    throw new IllegalArgumentException("Invalid CSV line format");
  }

  String idMachine = fields[0].trim();
  String operationName = fields[1].trim();
  int time;
  try {
    time = Integer.parseInt(fields[2].trim());
  } catch (NumberFormatException e) {
    throw new IllegalArgumentException("Invalid time value: " + fields[2]);
  }

  return new Machine(idMachine, operationName, time);
}

``````

**Complexity Analysis:**

* **Splitting the CSV Line:** csvLine.split(";") splits the input line by ; into an array of fields. The complexity is O(n), where n is the number of characters in csvLine.
* **Field Validation:** fields.length != 3 checks if there are exactly three fields, which is O(1).
* **Passing the operation name and time:** fields[1].trim() and fields[2].trim() remove any leading and trailing whitespace from the operation name and time fields. Since these fields are typically short, trimming is O(1) in this context. Integer.parseInt(fields[2].trim()) converts the time field to an integer. Parsing an integer from a string of length m has O(m) complexity. However, since the length of a time field is generally short, it’s considered O(1).
* **Returning the Machine:** Constructing and returning a new Machine instance is O(1).

**Total Complexity:** The overall complexity is dominated by the split operation, making the method O(n), where n is the number of characters in csvLine.


### USEI02

> **initializeTasks**

`````java
private void initializeTasks() {
    for (Item item : items) {
        if (item.hasMoreOperations()) {
            taskQueue.add(new Task(item, item.getNextOperation()));
            itemWorkstationHistory.put(item, new ArrayList<>());
            itemWaitingTimes.put(item.getIdItem(), 0); // Initialize waiting time for each item
        }
    }
}
``````
**Complexity Analysis:**

* **Loop over items:** If there are n items, this loop iterates n times.
* **Checking for operations**: (item.hasMoreOperations()) and adding tasks to the queue: Assuming these operations are constant time, each iteration performs O(1) work.

**Total Complexity:** O(n), where n is the number of items.

> **processTasks**

`````java
private void processTasks() {
  Iterator<Task> iterator = taskQueue.iterator();
  while (iterator.hasNext()) {
    Task task = iterator.next();
    Machine availableMachine = findFirstAvailableMachineForOperation(task.getOperation());

    if (availableMachine != null) {
      assignTaskToMachine(task, availableMachine);
      iterator.remove();
      itemWaitingStartTime.remove(task.getItem().getIdItem());
    } else {
      String itemId = task.getItem().getIdItem();
      if (!itemWaitingStartTime.containsKey(itemId)) {
        itemWaitingStartTime.put(itemId, currentTime);
      }
    }
  }
}
``````
**Complexity Analysis:**

* **Main Loop (While Loop):** If there are t tasks in the taskQueue, the loop runs O(t) times.
* **Finding the first available machine (findFirstAvailableMachineForOperation):**: This method has complexity O(m), where m is the number of machines.
* **Assigning a task to a machine(assignTaskToMachine):**  This method involves updating data structures, each operation within assignTaskToMachine has an average complexity of O(1).

**Total Complexity:** O(t*m), where t is the number of tasks and m is the number of machines.

> **findFirstAvailableMachineForOperation**

`````java
private Machine findFirstAvailableMachineForOperation(String operation) {
  return machines.stream()
          .filter(machine -> !busyMachines.containsKey(machine.getIdMachine()) && machine.getOperationName().equals(operation))
          .findFirst()
          .orElse(null);
}
``````
**Complexity Analysis:**

* **Filtering and searching through machines:** This iterates through the list of machines, and the complexity is O(m) since it checks each machine once.

**Total Complexity:** O(t*m), where t is the number of tasks and m is the number of machines.

> **assignTaskToMachine**

`````java
private void assignTaskToMachine(Task task, Machine machine) {
  busyMachines.put(machine.getIdMachine(), task);
  int taskDuration = machine.getTime();
  machine.setBusyUntil(currentTime + taskDuration);
  totalProductionTime += taskDuration;

  // Calculate waiting time and update statistics
  String itemId = task.getItem().getIdItem();
  if (itemWaitingStartTime.containsKey(itemId)) {
    int waitingTime = currentTime - itemWaitingStartTime.get(itemId);
    itemWaitingTimes.put(itemId, itemWaitingTimes.getOrDefault(itemId, 0) + waitingTime);
    itemWaitingStartTime.remove(itemId);
  }

  // Track execution time
  operationExecutionTimes.putIfAbsent(task.getOperation(), new ArrayList<>());
  operationExecutionTimes.get(task.getOperation()).add(taskDuration);

  // Increment task count
  operationTaskCounts.put(task.getOperation(), operationTaskCounts.getOrDefault(task.getOperation(), 0) + 1);

  machineOperationTimes.put(machine.getIdMachine(),
          machineOperationTimes.get(machine.getIdMachine()) + taskDuration);

  // Update history and time
  itemWorkstationHistory.get(task.getItem()).add(machine.getIdMachine());
  totalTimePerItem.put(itemId, totalTimePerItem.getOrDefault(itemId, 0) + taskDuration);
}
``````
**Complexity Analysis:**

* **Updating data structures:**  The put and get operations in HashMap and ArrayList are O(1).
* Checking and Updating each entry is done in constant time.

**Total Complexity:** O(1) per task assignment.

> **updateMachineAvailability**

`````java
private void updateMachineAvailability() {
  List<String> finishedMachines = new ArrayList<>();
  for (Map.Entry<String, Task> entry : busyMachines.entrySet()) {
    String machineId = entry.getKey();
    Machine machine = findMachineById(machineId);

    if (machine != null && machine.getAvailableTime() == currentTime) {
      finishedMachines.add(machineId);
      handleFinishedTask(machine, entry.getValue());
    }
  }

  // Remove machines that have finished tasks
  for (String machineId : finishedMachines) {
    busyMachines.remove(machineId);
  }
}
``````
**Complexity Analysis:**

* **Loop over busy machines:**  If there are b busy machines, the loop iterates O(b) times.
* **Finding each machine (findMachineById):** This method had O(1) complexity as it access a map of machines by its ID.
* **Removing finished machines:** Each removal operation on busyMachines has O(1) complexity.

**Total Complexity:** O(b), where b is the number of busy machines.

> **handleFinishedTask**

`````java
private void handleFinishedTask(Machine machine, Task finishedTask) {
  Item processedItem = finishedTask.getItem();
  if (processedItem.moveToNextOperation()) {
    String nextOperation = processedItem.getNextOperation();
    taskQueue.add(new Task(processedItem, nextOperation));
  } else {
    updateFlowDependency(itemWorkstationHistory.get(processedItem));
  }
}

``````
**Complexity Analysis:**

* **Moving to the next operation (processedItem.moveToNextOperation and item.getNextOperation):** These are be O(1) operations.
* **Updating the flow dependency (updateFlowDependency):** Based on prior analysis, updateFlowDependency has complexity O(w), where w is the length of workstationHistory for the item.

**Total Complexity:** O(1+w)=O(w), where w is the length of workstationHistory for the item.


### USEI03

> **presentTotalTimePerItem**

````java
      void presentTotalTimePerItem() {
        System.out.println("\n--- Total Time Spent Per Item ---");

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(totalTimePerItem.entrySet());
        sortedEntries.sort((entry1, entry2) -> Integer.compare(Integer.parseInt(entry1.getKey()), Integer.parseInt(entry2.getKey())));

        for (Map.Entry<String, Integer> entry : sortedEntries) {
        System.out.printf("Item: %s, Total Time Spent: %d units\n", entry.getKey(), entry.getValue());
        }
    }
````

**Execution Time**

* **Main Loop (forEach):** This iterates over the items in the totalTimePerItem map. If there are n items in the map, the loop runs n times.
* **Sorting the entries (sortedEntries.sort):** For each item, the sortedEntries list is sorted by key, which requires comparing the key values converted from String to Integer. Comparison-based sorting has a complexity of O(n log n).
* **Map Access:** Accessing entrySet() of totalTimePerItem to create the list of entries has a complexity of O(n), as it involves extracting all entries from the map.


### USEI04

> **presentTotalTimePerOperation**

````java
void presentTotalTimePerOperation() {
        System.out.println("\n--- Total Time and Percentage Spent Per Operation ---");

        // Calcula o tempo total gasto em todas as operações
        int overallTotalTime = operationExecutionTimes.values().stream()
        .flatMap(List::stream)
        .mapToInt(Integer::intValue)
        .sum();

        // Cria uma lista para armazenar as operações com o tempo total e a percentagem
        List<Map.Entry<String, Integer>> sortedEntries = operationExecutionTimes.entrySet().stream()
        .map(entry -> {
        String operation = entry.getKey();
        int totalTime = entry.getValue().stream().mapToInt(Integer::intValue).sum();
        return Map.entry(operation, totalTime);
        })
        .sorted(Comparator.comparingInt(Map.Entry::getValue)) // Ordena pelo tempo total de forma crescente
        .collect(Collectors.toList());

        // Exibe o tempo total e a percentagem para cada operação
        for (Map.Entry<String, Integer> entry : sortedEntries) {
        String operation = entry.getKey();
        int totalTime = entry.getValue();
        double percentage = (double) totalTime / overallTotalTime * 100;

        System.out.printf("Operation: %s, Total Time Spent: %d units, Usage Percentage: %.2f%%\n",
        operation, totalTime, percentage);
        }

        System.out.printf("\nTotal Time Across All Operations: %d units\n", overallTotalTime);
        }

````
**Execution Time**

* **Calculating the total time spent on all operations:** The method flatMap(List::stream).mapToInt(Integer::intValue).sum() traverses all execution time lists for all operations and calculates the total sum. If there are n operations and each operation has k execution times, this operation has a complexity of O(n⋅k), since it traverses all entries in each list.
* **Creating and sorting the sortedEntries list:** For each operation, the map() method calculates the total execution time with stream().mapToInt(Integer::intValue).sum(). This calculation has a complexity of O(k) per operation. Since there are n operations, this results in O(n⋅k). Then, the list of operations is sorted by total time using sorted(), and comparison-based sorting has a complexity of O(n log n).
* **Displaying the results:** The loop displaying the total time and percentage for each operation has a complexity of O(1) per operation and iterates n times, resulting in O(n) in total.
* **Total Complexity:** The complexity of the method is dominated by calculating the total time for all operations O(n⋅k) and sorting the operations, which has a complexity of O(n log n). Thus, the total complexity of the method is O(n⋅k + n log n).


### USEI05

* presentMachineUtilizationReport

````java
 void presentMachineUtilizationReport() {
  System.out.println("\n--- Machine Usage Report (Relative to Total Usage) ---");

  double totalOperationTime = machineOperationTimes.values().stream().mapToInt(Integer::intValue).sum();

  // Map to store total time spent per operation type
  Map<String, Integer> totalTimeByOperationType = new HashMap<>();

  // Populate the map with total time by operation type
  for (Machine machine : machines) {
    int machineTime = machineOperationTimes.get(machine.getIdMachine());
    totalTimeByOperationType.put(machine.getOperationName(),
            totalTimeByOperationType.getOrDefault(machine.getOperationName(), 0) + machineTime);
  }

  // Create a list of machine utilization
  List<MachineUtilization> machineUtilizations = machines.stream()
          .map(machine -> {
            int machineTime = machineOperationTimes.get(machine.getIdMachine());
            double usagePercentage = (totalOperationTime > 0) ? (machineTime / totalOperationTime) * 100 : 0;
            double operationTypePercentage = (totalTimeByOperationType.get(machine.getOperationName()) > 0)
                    ? (machineTime / (double) totalTimeByOperationType.get(machine.getOperationName())) * 100
                    : 0;
            return new MachineUtilization(machine.getIdMachine(), machineTime, usagePercentage, operationTypePercentage);
          })
          .sorted(Comparator.comparingDouble(MachineUtilization::getUtilizationPercentage))
          .collect(Collectors.toList());

  // Display the machine utilization data
  machineUtilizations.forEach(machineUtilization -> {
    Machine machine = findMachineById(machineUtilization.getIdMachine());
    String operationName = machine != null ? machine.getOperationName() : "Unknown";
    System.out.printf("Machine: %s, Total Operation Time: %d seconds, Usage: %.2f%%, Operation Type (%s) Usage: %.2f%%\n",
            machineUtilization.getIdMachine(), machineUtilization.getTotalTime(),
            machineUtilization.getUtilizationPercentage(), operationName, machineUtilization.getOperationTypeUsage());
  });

  System.out.printf("\nTotal Operation Time Across All Machines: %.0f seconds\n", totalOperationTime);
}
````

**Execution Time**

* **Calculating the total time spent across all operations:** This operation sums up the total machine operation times. If there are m machines, this requires iterating over all machine times, resulting in a complexity of O(m).
* **Populating totalTimeByOperationType:** This loop iterates over all machines to populate a map that tracks the total time spent on each operation type. Complexity: O(m), since there is one iteration for each machine.
* **Creating and Sorting the machine Utilizations List:** Mapping Step: The map() operation iterates over all machines to create MachineUtilization objects, resulting in O(m). Sorting Step: Sorting the list of MachineUtilization objects by utilization percentage has a complexity of O(m log m).
* **Displaying the Machine Utilization Data:** This step iterates over the machineUtilizations list to print the details for each machine. The complexity is O(m).
* **Displaying the Total Operation Time:** This is a simple print operation with a complexity of O(1).

* **Total Complexity:** The method's complexity is dominated by the sorting step, which has a complexity of O(m log m). The other operations are linear with a complexity of O(m). Thus, the total complexity of the presentMachineUtilizationReport method is O(m log m). This reflects the most computationally intensive operation in the method, which is sorting the list of machines based on their utilization percentages.


### USEI06

> **presentOperationTimesReport**

````java
  void presentOperationTimesReport() {
  System.out.println("\n--- Operation Execution and Waiting Times Report ---");

  operationExecutionTimes.forEach((operation, times) -> {
    double averageTime = times.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    int totalWaitingTime = operationWaitingTimes.getOrDefault(operation, 0);
    int taskCount = operationTaskCounts.getOrDefault(operation, 1);
    double averageWaitingTime = (double) totalWaitingTime / taskCount;

    System.out.printf("Operation: %s, Average Execution Time: %.2f units\n",
            operation, averageTime);
  });
}

````
**Execution Time**

* **Main loop (forEach):** The method iterates over the operationExecutionTimes map, where each key represents an operation, and each value is a list of execution times. If there are n operations in the map, the loop executes n times.
* **Calculating the average execution time:** For each operation, the average time is calculated using stream().mapToInt(Integer::intValue).average().orElse(0.0), which traverses the list of execution times and calculates the average. If k is the number of execution times per operation, the complexity of this calculation is O(k).
* **Accessing the operationWaitingTimes and operationTaskCounts maps:** For each operation, the values of operationWaitingTimes and operationTaskCounts are retrieved using the getOrDefault() method. Since querying a map has a complexity of O(1) on average, accessing these values for each operation is constant, resulting in O(n) in total for all operations.
* **Displaying the results:** The System.out.printf() method is called for each operation, with a complexity of O(1) per call. Since this happens for n operations, the total complexity of this step is O(n).
* **Total Complexity:** The complexity of the method is dominated by calculating the average execution time for all operations, which is O(n⋅k), where n is the number of operations, and k is the number of execution times per operation. Accessing the maps and displaying the results have linear complexity, O(n), but do not surpass the cost of calculating the average time. Therefore, the total complexity of the method is O(n⋅k).


> **presentTotalWaitTimePerItem**

````java
 void presentTotalWaitTimePerItem() {
  System.out.println("\n--- Total Waiting Time Per Item ---");


  List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(itemWaitingTimes.entrySet());
  sortedEntries.sort(Comparator.comparingInt(entry -> Integer.parseInt(entry.getKey())));


  for (Map.Entry<String, Integer> entry : sortedEntries) {
    System.out.printf("Item: %s, Total Waiting Per Time: %d units\n", entry.getKey(), entry.getValue());
  }
}

````
**Execution Time**

* **Creating the List of Entries:** This step creates a new ArrayList containing all the entries from the itemWaitingTimes map. If there are n items, this operation has a complexity of O(n).
* **Sorting the List of Entries:** Sorting the list of n entries based on the item key. Comparison-based sorting algorithms have a time complexity of O(n log n).
* **Displaying the Results:** This loop iterates over all the sorted entries to display the total waiting time for each item. The complexity of this step is O(n).

* **Total Complexity:** The overall complexity is dominated by the sorting step, which has a complexity of O(n log n). The other operations are linear, with a complexity of O(n) each. Therefore, the total complexity of the presentTotalWaitTimePerItem method is: O(n log n). This reflects the most computationally expensive operation in the method, which is sorting the list of entries based on the item keys.


### USEI07

* updateFlowDependency e presentWorkstationFlowReport

````java
private void updateFlowDependency(List<String> workstationHistory) {
        if (workstationHistory.size() < 2) return;

        for (int i = 0; i < workstationHistory.size() - 1; i++) {
            String from = workstationHistory.get(i);
            String to = workstationHistory.get(i + 1);

            flowDependencyMap.putIfAbsent(from, new HashMap<>());
            Map<String, Integer> transitions = flowDependencyMap.get(from);
            transitions.put(to, transitions.getOrDefault(to, 0) + 1);
        }
    }

    // Present the flow dependency report
    void presentWorkstationFlowReport() {
        System.out.println("\n--- Workstation Flow Dependency Report ---");

        flowDependencyMap.forEach((workstation, transitions) -> {
            List<Map.Entry<String, Integer>> sortedTransitions = transitions.entrySet().stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))  // Sort by descending order of processed items
                    .collect(Collectors.toList());

            System.out.printf("%s : %s\n", workstation, sortedTransitions);
        });
    }
````

**Execution Time**

**1. updateFlowDependency**

* **workstationHistory:** Checking the size of workstationHistory is a constant operation, so the complexity is O(1).
* **Main Loop:** The loop iterates over the list workstationHistory n-1 times, where n is the length of workstationHistory. Each iteration performs:
  * A possible insertion into flowDependencyMap, which is typically O(1) for a HashMap.
  * A lookup and update for the transition count in the inner map (transitions), which is also O(1).
  * Therefore, each iteration of the loop is O(1), and the total complexity of the loop is O(n), where n is the size of workstationHistory.
  

**2. presentWorkstationFlowReport**

* **Iteration over flowDependencyMap:** The outer forEach loop iterates over all workstations in the flowDependencyMap. For each workstation:
  * Accessing the map of transitions is O(1).
  * Sorting the transitions requires O(m log m), where m is the number of transitions for that workstation. Sorting is based on the count of transitions.
  * Collecting the sorted transitions into a list is O(m).
  
* The total complexity for each workstation is therefore O(m log m), and since m varies between workstations, the complexity can be generalized as the sum of all transitions across all workstations.
* Printing the report: Printing each sorted list has a complexity of O(1) per transition, leading to O(m) for all transitions.
* Therefore, the overall complexity for presentWorkstationFlowReport is O(n log n), considering the sum of all transitions across all workstations.


### USEI08

> **processTasks**

`````java
private void processTasks() {
  Iterator<Task> iterator = taskQueue.iterator();
  Set<String> itemsWithWaitingTimeUpdated = new HashSet<>(); // Track items that had waiting time updated

  while (iterator.hasNext()) {
    Task task = iterator.next();
    Machine availableMachine = findFastestAvailableMachineForOperation(task.getOperation());

    if (availableMachine != null) {
      assignTaskToMachine(task, availableMachine);
      iterator.remove();
    } else {
      // Only increment waiting time if the item has not already been updated in this cycle
      String itemId = task.getItem().getIdItem();
      if (!itemsWithWaitingTimeUpdated.contains(itemId)) {
        operationWaitingTimes.put(task.getOperation(),
                operationWaitingTimes.getOrDefault(task.getOperation(), 0) + 1); // Increment waiting time

        // Updates the accumulated waiting time for the item
        itemWaitingTimes.put(itemId, itemWaitingTimes.getOrDefault(itemId, 0) + 1);

        // Mark the item as updated for this cycle
        itemsWithWaitingTimeUpdated.add(itemId);
      }
    }
  }
}

``````
**Complexity Analysis:**

* **Main Loop (While Loop):** If there are t tasks in the taskQueue, the loop runs O(t) times.
* **Method findFastestAvailableMachineForOperation** This method has complexity O(m), where m is the number of machines (explained below).
* **Task Assignment (assignTaskToMachine):**: This method performs constant-time operations, O(1).
* **Checking and Updating itemsWithWaitingTimeUpdated:**  Both checking (contains) and the put and add operations on HashSet and HashMap have an average complexity of O(1).

**Total Complexity:** O(t * m), where t is the number of tasks and m is the number of machines. The factor O(m) arises from finding the fastest available machine for each task.


> **findFirstAvailableMachineForOperation**

`````java
private Machine findFastestAvailableMachineForOperation(String operation) {
  return machines.stream()
          .filter(machine -> !busyMachines.containsKey(machine.getIdMachine()) && machine.getOperationName().equals(operation))
          .min(Comparator.comparingInt(Machine::getAvailableTime)
                  .thenComparingInt(Machine::getTime))
          .orElse(null);
}

``````
**Complexity Analysis:**

* **Filtering (filter):** This process iterates over the list of machines, with a complexity of O(m),
* **Finding the minimum (min):** The operation to find the machine with the minimum available time has a complexity of O(m), as in the worst case, all machines need to be checked.

**Total Complexity:** O(m), where m is the number of machines. The method goes through all machines to apply the filters and find the fastest available machine.


> **assignTaskToMachine**

`````java
private void assignTaskToMachine(Task task, Machine machine) {
  busyMachines.put(machine.getIdMachine(), task);
  int taskDuration = machine.getTime();
  machine.setBusyUntil(currentTime + taskDuration);
  totalProductionTime += taskDuration;

  // Updates the total time spent on the item
  String itemId = task.getItem().getIdItem();
  totalTimePerItem.put(itemId, totalTimePerItem.getOrDefault(itemId, 0) + taskDuration);

  // Track execution time for each operation
  operationExecutionTimes.putIfAbsent(task.getOperation(), new ArrayList<>());
  operationExecutionTimes.get(task.getOperation()).add(taskDuration);

  // Increment the task count for this operation
  operationTaskCounts.put(task.getOperation(), operationTaskCounts.getOrDefault(task.getOperation(), 0) + 1);

  machineOperationTimes.put(machine.getIdMachine(),
          machineOperationTimes.get(machine.getIdMachine()) + taskDuration);

  // Update the workstation history for the item
  itemWorkstationHistory.get(task.getItem()).add(machine.getIdMachine());

  // Log the task assignment
  System.out.println("--------------------------------------------------");
  System.out.printf("Time: %d - Machine %s is processing:\n", currentTime, machine.getIdMachine());
  System.out.printf("   Item: %s (Priority: %s)\n", task.getItem().getIdItem(), task.getItem().getPriority());
  System.out.printf("   Operation: %s (%d units of time)\n", task.getOperation(), taskDuration);
  System.out.printf("   Expected to finish by time: %d\n", machine.getAvailableTime());
  System.out.println("--------------------------------------------------");
}

``````
**Complexity Analysis:**

* **Operations with HashMap and ArrayList:** Insertion and retrieval operations on HashMap and ArrayList have an average complexity of O(1).
* **Updates and Insertions in Data Structures:** All operations within the method are performed in constant time.

**Total Complexity:** O(1) per task. The method performs a fixed amount of constant-time operations.
