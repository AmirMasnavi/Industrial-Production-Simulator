package org.example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SimulatorNoPriorities simulates the processing of items through various machines without considering priorities.
 * It manages the assignment of tasks to available machines, tracks execution and waiting times, and provides reports
 * on machine utilization and operational dependencies.
 */
public class SimulatorNoPriorities {
    private final List<Item> items;  // List of items to be processed
    private final List<Machine> machines;  // List of available machines
    private final Queue<Task> taskQueue;  // Queue of tasks to be processed
    private final Map<String, Task> busyMachines;  // Map to track tasks currently being processed by machines
    private int currentTime;  // Current time step in the simulation
    private final Map<String, Integer> machineOperationTimes;  // Total operation times per machine
    private int totalProductionTime = 0;  // Total production time for all items
    private final Map<String, List<Integer>> operationExecutionTimes;  // Execution times for each operation
    private final Map<String, Integer> operationWaitingTimes;  // Waiting times for each operation
    private final Map<String, Integer> operationTaskCounts;  // Track the number of tasks for each operation

    private final Map<String, Map<String, Integer>> flowDependencyMap;  // Map to track the flow dependencies between workstations
    private final Map<Item, List<String>> itemWorkstationHistory;  // History of workstations visited by each item
    private final Map<String, Integer> totalTimePerItem;  // Total time spent processing each item
    private final Map<String, Integer> itemWaitingTimes;  // Waiting times for each item
    private final Map<String, Integer> itemWaitingStartTime;  // Start time of the waiting period for each item

    /**
     * Constructor for SimulatorNoPriorities.
     *
     * @param items    List of items to process.
     * @param machines List of machines available for processing.
     */
    public SimulatorNoPriorities(List<Item> items, List<Machine> machines) {
        this.items = items;
        this.machines = machines;
        this.taskQueue = new LinkedList<>();  // Simple queue for tasks
        this.busyMachines = new HashMap<>();
        this.machineOperationTimes = new HashMap<>();
        this.operationExecutionTimes = new HashMap<>();
        this.operationWaitingTimes = new HashMap<>();
        this.operationTaskCounts = new HashMap<>();  // Initialize task count for each operation
        this.flowDependencyMap = new HashMap<>();
        this.itemWorkstationHistory = new HashMap<>();
        this.currentTime = 0;
        this.totalTimePerItem = new HashMap<>();
        this.itemWaitingTimes = new HashMap<>(); // Initialize map for item wait times
        this.itemWaitingStartTime = new HashMap<>();

        for (Machine machine : machines) {
            machineOperationTimes.put(machine.getIdMachine(), 0);
        }

        initializeTasks();
    }

    /**
     * Initialize the task queue with the first operation of each item.
     */
    private void initializeTasks() {
        for (Item item : items) {
            if (item.hasMoreOperations()) {
                taskQueue.add(new Task(item, item.getNextOperation()));
                itemWorkstationHistory.put(item, new ArrayList<>());
                itemWaitingTimes.put(item.getIdItem(), 0); // Initialize waiting time for each item
            }
        }
    }

    /**
     * Display the list of items and their operations before the simulation starts.
     */
    void presentInitialItemList() {
        for (Item item : items) {
            System.out.printf("Item ID: %s, Priority: %s\n", item.getIdItem(), item.getPriority());

            // Display the list of operations directly
            List<String> operations = item.operations; // Access the list of operations directly
            for (int i = 0; i < operations.size(); i++) {
                System.out.printf("   Operation %d: %s\n", i + 1, operations.get(i));
            }
            System.out.println("\n");
        }
    }

    /**
     * Main simulation loop. Processes tasks and updates machine availability.
     */
    public void runSimulation() {
        System.out.println("\n=== Items to be Processed in the Simulation ===\n");
        presentInitialItemList();
        System.out.println("\n=== Starting the Simulation ===");
        while (!taskQueue.isEmpty() || !busyMachines.isEmpty()) {
            processTasks();
            updateMachineAvailability();
            currentTime++;
        }
        System.out.println("=== Simulation Finished ===");
    }

    /**
     * Process tasks from the queue and assign them to available machines.
     */
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

    /**
     * Find the first available machine for the given operation.
     *
     * @param operation The operation to be performed.
     * @return The first available machine that can perform the operation, or null if none are available.
     */
    private Machine findFirstAvailableMachineForOperation(String operation) {
        return machines.stream()
                .filter(machine -> !busyMachines.containsKey(machine.getIdMachine()) && machine.getOperationName().equals(operation))
                .findFirst()
                .orElse(null);
    }

    /**
     * Assign a task to the selected machine.
     *
     * @param task    The task to assign.
     * @param machine The machine to assign the task to.
     */
    private void assignTaskToMachine(Task task, Machine machine) {
        busyMachines.put(machine.getIdMachine(), task);
        int taskDuration = machine.getTime();
        machine.setBusyUntil(currentTime + taskDuration);
        totalProductionTime += taskDuration;

        // Calculate the waiting time for the item and add it to the total waiting time
        String itemId = task.getItem().getIdItem();
        if (itemWaitingStartTime.containsKey(itemId)) {
            int waitingTime = currentTime - itemWaitingStartTime.get(itemId);
            itemWaitingTimes.put(itemId, itemWaitingTimes.getOrDefault(itemId, 0) + waitingTime);
            itemWaitingStartTime.remove(itemId); // Clear the start time of waiting
        }

        // Track execution time for each operation
        operationExecutionTimes.putIfAbsent(task.getOperation(), new ArrayList<>());
        operationExecutionTimes.get(task.getOperation()).add(taskDuration);

        // Increment task count for this operation
        operationTaskCounts.put(task.getOperation(), operationTaskCounts.getOrDefault(task.getOperation(), 0) + 1);

        machineOperationTimes.put(machine.getIdMachine(),
                machineOperationTimes.get(machine.getIdMachine()) + taskDuration);

        // Update the item's workstation history
        itemWorkstationHistory.get(task.getItem()).add(machine.getIdMachine());

        // Update the total time spent per item
        totalTimePerItem.put(itemId, totalTimePerItem.getOrDefault(itemId, 0) + taskDuration);

        // Log task assignment
        System.out.println("--------------------------------------------------");
        System.out.printf("Time: %d - Machine %s is processing:\n", currentTime, machine.getIdMachine());
        System.out.printf("   Item: %s\n", task.getItem().getIdItem());
        System.out.printf("   Operation: %s (%d units of time)\n", task.getOperation(), taskDuration);
        System.out.printf("   Expected to finish by time: %d\n", machine.getAvailableTime());
        System.out.println("--------------------------------------------------");
    }

    /**
     * Update the availability of machines that are finishing tasks at the current time.
     */
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

        // Remove machines from the busy list that have finished their tasks
        for (String machineId : finishedMachines) {
            busyMachines.remove(machineId);
        }
    }

    /**
     * Handle a finished task by updating the simulation state.
     * If the item has more operations, add the next one to the task queue.
     * If the item has completed all its operations, update the flow dependency.
     *
     * @param machine      the machine that has finished its task
     * @param finishedTask the task that was completed
     */
    private void handleFinishedTask(Machine machine, Task finishedTask) {
        System.out.printf("Time: %d - Machine %s has finished its task and is now available.\n", currentTime, machine.getIdMachine());

        Item processedItem = finishedTask.getItem();
        if (processedItem.moveToNextOperation()) {
            String nextOperation = processedItem.getNextOperation();
            taskQueue.add(new Task(processedItem, nextOperation));
            System.out.printf("Time: %d - Added %s of item %s to the queue\n",
                    currentTime, nextOperation, processedItem.getIdItem());
        } else {
            updateFlowDependency(itemWorkstationHistory.get(processedItem));
            System.out.printf("Time: %d - Item %s has completed all its operations!\n", currentTime, processedItem.getIdItem());
        }
    }

    /**
     * Find a machine by its unique identifier.
     *
     * @param machineId the identifier of the machine to be found
     * @return the Machine object if found, otherwise null
     */
    private Machine findMachineById(String machineId) {
        return machines.stream()
                .filter(machine -> machine.getIdMachine().equals(machineId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Update the flow dependency map, which tracks transitions between workstations.
     * If an item has moved through multiple workstations, record the transitions.
     *
     * @param workstationHistory the list of workstations that an item has visited
     */
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

    /**
     * Display the workstation flow dependency report.
     * Shows the number of transitions between workstations in the order of frequency.
     */
    void presentWorkstationFlowReport() {
        System.out.println("\n--- Workstation Flow Dependency Report ---");

        flowDependencyMap.keySet().stream()
                .sorted()
                .forEach(workstation -> {
                    List<Map.Entry<String, Integer>> sortedTransitions = flowDependencyMap.get(workstation).entrySet().stream()
                            .sorted((a, b) -> b.getValue().compareTo(a.getValue())) // Sort by descending order of processed items
                            .collect(Collectors.toList());

                    String formattedTransitions = sortedTransitions.stream()
                            .map(entry -> String.format("(%s,%d)", entry.getKey(), entry.getValue())) // Format each transition
                            .collect(Collectors.joining(",")); // Join transitions with commas

                    System.out.printf("%s : [%s]\n", workstation, formattedTransitions);
                });
    }

    /**
     * Display the machine utilization report showing the relative usage of each machine.
     */
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

    /**
     * Display the report of average execution times per operation.
     */
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

    /**
     * Display the total time and percentage spent on each operation.
     */
    void presentTotalTimePerOperation() {
        System.out.println("\n--- Total Time and Percentage Spent Per Operation ---");

        // Calculate total time spent on all operations
        int overallTotalTime = operationExecutionTimes.values().stream()
                .flatMap(List::stream)
                .mapToInt(Integer::intValue)
                .sum();

        // Create a list of operations with total time and percentage
        List<Map.Entry<String, Integer>> sortedEntries = operationExecutionTimes.entrySet().stream()
                .map(entry -> {
                    String operation = entry.getKey();
                    int totalTime = entry.getValue().stream().mapToInt(Integer::intValue).sum();
                    return Map.entry(operation, totalTime);
                })
                .sorted(Comparator.comparingInt(Map.Entry::getValue)) // Sort by total time in ascending order
                .collect(Collectors.toList());

        // Display total time and percentage for each operation
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String operation = entry.getKey();
            int totalTime = entry.getValue();
            double percentage = (double) totalTime / overallTotalTime * 100;

            System.out.printf("Operation: %s, Total Time Spent: %d units, Usage Percentage: %.2f%%\n",
                    operation, totalTime, percentage);
        }

        System.out.printf("\nTotal Time Across All Operations: %d units\n", overallTotalTime);
    }

    /**
     * Display the total time spent on each item.
     */
    void presentTotalTimePerItem() {
        System.out.println("\n--- Total Time Spent Per Item ---");

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(totalTimePerItem.entrySet());
        sortedEntries.sort((entry1, entry2) -> Integer.compare(Integer.parseInt(entry1.getKey()), Integer.parseInt(entry2.getKey())));

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            System.out.printf("Item: %s, Total Time Spent: %d units\n", entry.getKey(), entry.getValue());
        }
    }

    /**
     * Display the total waiting time per item.
     */
    void presentTotalWaitTimePerItem() {
        System.out.println("\n--- Total Waiting Time Per Item ---");


        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(itemWaitingTimes.entrySet());
        sortedEntries.sort(Comparator.comparingInt(entry -> Integer.parseInt(entry.getKey())));


        for (Map.Entry<String, Integer> entry : sortedEntries) {
            System.out.printf("Item: %s, Total Waiting Per Time: %d units\n", entry.getKey(), entry.getValue());
        }
    }



}