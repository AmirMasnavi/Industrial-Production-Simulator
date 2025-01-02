package org.example;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The Simulator class simulates the processing of items through multiple machines.
 * Each item has a sequence of operations that need to be completed, and machines
 * have different capabilities and availability to perform these operations. The
 * class manages task assignment, machine utilization, and keeps track of various
 * metrics such as operation times and machine usage.
 */
public class Simulator {
    private final List<Article> articles; // List of items to be processed
    private final List<Machine> machines; // List of machines available for processing
    private final PriorityQueue<Task> taskQueue; // Queue for tasks to be executed, prioritized by task priority
    private final Map<String, Task> busyMachines; // Tracks which machines are currently busy
    private int currentTime; // Current time in the simulation
    private final Map<String, Integer> machineOperationTimes; // Cumulative operation times for each machine
    private int totalProductionTime = 0; // Total production time across all tasks
    private final Map<String, List<Integer>> operationExecutionTimes; // Execution times for each operation
    private final Map<String, Integer> operationWaitingTimes; // Waiting times for each operation
    private final Map<String, Integer> operationTaskCounts; // Count of tasks executed for each operation

    private final Map<String, Map<String, Integer>> flowDependencyMap; // Tracks transitions between workstations
    private final Map<Article, List<String>> itemWorkstationHistory; // History of workstations used by each item

    private final Map<String, Integer> totalTimePerItem; // Total processing time for each item

    private final Map<String, Integer> itemWaitingTimes; // Accumulated waiting times for each item
    private final DatabaseConnection dbConnection; //DB connection

    /**
     * Constructor to initialize the simulator with a list of items and machines.
     *
     * @param articles The list of items to be processed.
     * @param machines The list of machines available for processing.
     */
    public Simulator(List<Article> articles, List<Machine> machines, DatabaseConnection dbConnection) {
        this.articles = articles;
        this.machines = machines;
        this.dbConnection = dbConnection;
        this.taskQueue = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority).reversed());
        this.busyMachines = new HashMap<>();
        this.machineOperationTimes = new HashMap<>();
        this.operationExecutionTimes = new HashMap<>();
        this.operationWaitingTimes = new HashMap<>();
        this.operationTaskCounts = new HashMap<>();
        this.flowDependencyMap = new HashMap<>();
        this.itemWorkstationHistory = new HashMap<>();
        this.currentTime = 0;
        this.totalTimePerItem = new HashMap<>();
        this.itemWaitingTimes = new HashMap<>();

        for (Machine machine : machines) {
            machineOperationTimes.put(machine.getIdMachine(), 0);
        }

        initializeTasks();
    }

    /**
     * Initializes the task queue with the first operation of each item.
     */
    public void initializeTasks() {
        for (Article article : this.articles) {
            if (article.hasMoreOperations()) {
                taskQueue.add(new Task(article, article.getNextOperation()));
                itemWorkstationHistory.put(article, new ArrayList<>());
            }
        }
    }

    /**
     * Displays the initial list of items ordered by processing priority.
     */
    void presentInitialItemList() {
        System.out.println("\n--- Initial List of Items (Order by Processing Priority) ---");

        // Creates a sorted copy of the items list based on priority
        List<Article> sortedArticles = new ArrayList<>(articles);
        sortedArticles.sort(Comparator.comparing(Article::getPriority).reversed());

        // Displays the items in the order they will be processed
        for (Article article : sortedArticles) {
            System.out.printf("Item ID: %s, Priority: %s\n", article.getIdItem(), article.getPriority());

            // Displays the list of operations for the item
            List<String> operations = article.operations;
            for (int i = 0; i < operations.size(); i++) {
                System.out.printf("   Operation %d: %s\n", i + 1, operations.get(i));
            }
            System.out.println();
        }
    }

    /**
     * Runs the main simulation loop, processing tasks and updating machine availability.
     */
    public void runSimulation() {
        System.out.println("\n=== Items to be Processed in the Simulation ===\n");
        presentInitialItemList();

        System.out.println("\n=== Starting the Simulation ===");

        // Main simulation loop
        while (!taskQueue.isEmpty() || !busyMachines.isEmpty()) {
            processTasks();
            updateMachineAvailability();
            currentTime++;
        }

        System.out.println("=== Simulation Finished ===");

        // Present operation times report
        presentOperationTimesReport();

        int currentSimulationID = 1;

        String getMaxSimulationIDQuery = "SELECT COALESCE(MAX(SimulationID), 0) AS MaxSimulationID FROM Simulation";
        try {
            ResultSet resultSet = dbConnection.executeQuery( getMaxSimulationIDQuery, new Object[]{});
            if (resultSet.next()) {
                currentSimulationID = resultSet.getInt("MaxSimulationID") + 1;
            }
        } catch (Exception e) {
            System.err.println("Error searching for new SimulationID: " + e.getMessage());
            return; // Retornar para evitar continuar com um ID inválido
        }

        System.out.println("Beginning simulation with current ID: " + currentSimulationID);


        String simulationInsertQuery = "INSERT INTO Simulation (SimulationID, SimulationDate) VALUES (?, ?)";
        try {
            dbConnection.executeUpdate(
                    simulationInsertQuery,
                    new Object[] { currentSimulationID, new java.sql.Date(System.currentTimeMillis()) }
            );
        } catch (Exception e) {
            System.err.println("Error inserting into Simulation table: " + e.getMessage());
        }

        try {
            System.out.println("\n=== Updating Database with Simulation Operation Totals ===");

            String updateQuery = String.format(
                    "MERGE INTO Simulation_Operation_Totals a " +
                            "USING DUAL " +
                            "ON (a.SimulationID = ? AND a.OperationID = ?) " +
                            "WHEN MATCHED THEN " +
                            "  UPDATE SET " +
                            "    Total_Execution_Time = a.Total_Execution_Time + ?, " +
                            "    Total_Waiting_Time = a.Total_Waiting_Time + ?, " +
                            "    Task_Count = a.Task_Count + ? " +
                            "WHEN NOT MATCHED THEN " +
                            "  INSERT (SimulationID, OperationID, Total_Execution_Time, Total_Waiting_Time, Task_Count) " +
                            "  VALUES (?, ?, ?, ?, ?)"
            );

            for (Map.Entry<String, List<Integer>> entry : operationExecutionTimes.entrySet()) {
                String operation = entry.getKey();
                List<Integer> times = entry.getValue();

                int totalExecutionTime = times.stream().mapToInt(Integer::intValue).sum();
                int totalWaitingTime = operationWaitingTimes.getOrDefault(operation, 0);
                int taskCount = operationTaskCounts.getOrDefault(operation, 0);

                // Parameters for the prepared statement
                Object[] params = {
                        currentSimulationID, // SimulationID
                        operation,           // OperationID
                        totalExecutionTime,  // Total_Execution_Time for update
                        totalWaitingTime,    // Total_Waiting_Time for update
                        taskCount,           // Task_Count for update
                        currentSimulationID, // SimulationID for insert
                        operation,           // OperationID for insert
                        totalExecutionTime,  // Total_Execution_Time for insert
                        totalWaitingTime,    // Total_Waiting_Time for insert
                        taskCount            // Task_Count for insert
                };

                dbConnection.executeUpdate(updateQuery, params);

                System.out.printf(
                        "Updated database for operation '%s': Total Exec Time = %d, Total Wait Time = %d, Task Count = %d%n",
                        operation, totalExecutionTime, totalWaitingTime, taskCount
                );
            }

            System.out.println("Database update completed successfully!");
            calculateAndStoreAverageProductionTime(currentSimulationID);
        } catch (Exception e) {
            System.err.println("Error updating database: " + e.getMessage());
        }

        // Present averages report dynamically
        presentAverageTimesReport(currentSimulationID);

    }


    public void presentAverageTimesReport(int simulationID) {
        String query = "SELECT OperationID, " +
                "Total_Execution_Time / Task_Count AS Avg_Execution_Time, " +
                "Total_Waiting_Time / Task_Count AS Avg_Waiting_Time " +
                "FROM Simulation_Operation_Totals " +
                "WHERE SimulationID = ?";

        try {
            ResultSet resultSet = dbConnection.executeQuery(query, new Object[]{simulationID});
            System.out.println("\n=== Average Times Report ===");

            while (resultSet.next()) {
                String operationID = resultSet.getString("OperationID");
                double avgExecTime = resultSet.getDouble("Avg_Execution_Time");
                double avgWaitTime = resultSet.getDouble("Avg_Waiting_Time");

                System.out.printf("Operation %s: Avg Exec Time = %.2f, Avg Wait Time = %.2f%n",
                        operationID, avgExecTime, avgWaitTime);
            }
        } catch (Exception e) {
            System.err.println("Error fetching average times: " + e.getMessage());
        }
    }

    public void calculateAndStoreAverageProductionTime(int simulationID) {
        String query = "SELECT SUM(Total_Execution_Time) AS TotalExecTime, " +
                "SUM(Total_Waiting_Time) AS TotalWaitTime, " +
                "COUNT(OperationID) AS OperationCount " +
                "FROM Simulation_Operation_Totals " +
                "WHERE SimulationID = ?";

        try {
            ResultSet resultSet = dbConnection.executeQuery(query, new Object[]{simulationID});
            if (resultSet.next()) {
                int totalExecTime = resultSet.getInt("TotalExecTime");
                int totalWaitTime = resultSet.getInt("TotalWaitTime");
                int operationCount = resultSet.getInt("OperationCount");

                // Evitar divisão por zero
                if (operationCount == 0) {
                    System.err.println("Nenhuma operação encontrada para a simulação ID " + simulationID);
                    return;
                }

                double averageProductionTime = (double) (totalExecTime + totalWaitTime) / operationCount;

                // Inserir na nova tabela
                String insertQuery = "INSERT INTO Simulation_Average_Production_Time (SimulationID, Average_Production_Time) " +
                        "VALUES (?, ?)";
                dbConnection.executeUpdate(insertQuery, new Object[]{simulationID, averageProductionTime});

                System.out.printf("Simulação %d: Average Production Time = %.2f%n", simulationID, averageProductionTime);
            }
        } catch (Exception e) {
            System.err.println("Erro ao calcular ou armazenar o tempo médio de produção: " + e.getMessage());
        }
    }



    /**
         * Processes tasks from the queue and assigns them to available machines.
         */
        private void processTasks () {
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

        /**
         * Finds the fastest available machine capable of performing the specified operation.
         *
         * @param operation The operation to be performed.
         * @return The fastest available machine for the operation, or null if none are available.
         */
        private Machine findFastestAvailableMachineForOperation (String operation){
            return machines.stream()
                    .filter(machine -> !busyMachines.containsKey(machine.getIdMachine()) && machine.getOperationName().equals(operation))
                    .min(Comparator.comparingInt(Machine::getAvailableTime)
                            .thenComparingInt(Machine::getTime))
                    .orElse(null);
        }

        /**
         * Assigns a task to a selected machine and tracks execution time for each item.
         *
         * @param task    The task to be assigned.
         * @param machine The machine to which the task is assigned.
         */

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

        // Write task assignment to CSV
        writeTaskToCsv(machine.getIdMachine(), task.getItem().getIdItem(), task.getOperation(), currentTime, taskDuration);
    }

    /**
     * Writes a task assignment to the plannerSimulator.csv file.
     *
     * @param machineId    the ID of the machine
     * @param itemId       the ID of the item
     * @param operation    the name of the operation
     * @param startTime    the start time of the task
     * @param duration     the duration of the task
     */
    private void writeTaskToCsv(String machineId, String itemId, String operation, int startTime, int duration) {
        String csvFile = "plannerSimulator.csv"; // File path
        File file = new File(csvFile);

        try (FileWriter writer = new FileWriter(file, true)) {
            // If the file is new or empty, write the header
            if (file.length() == 0) {
                writer.append("Machine_ID,Item_ID,Operation_Name,Start_Time,Duration\n");
            }

            // Append a new line to the CSV file
            writer.append(String.format("%s,%s,%s,%d,%d%n", machineId, itemId, operation, startTime, duration));
        } catch (IOException e) {
            System.err.println("Error writing to plannerSimulator.csv: " + e.getMessage());
        }
    }


    /**
         * Updates the availability of machines that are finishing tasks at the current time.
         */
        private void updateMachineAvailability () {
            List<String> finishedMachines = new ArrayList<>();
            for (Map.Entry<String, Task> entry : busyMachines.entrySet()) {
                String machineId = entry.getKey();
                Machine machine = findMachineById(machineId);

                if (machine != null && machine.getAvailableTime() == currentTime) {
                    finishedMachines.add(machineId);
                    handleFinishedTask(machine, entry.getValue());
                }
            }

            for (String machineId : finishedMachines) {
                busyMachines.remove(machineId);
            }
        }

        /**
         * Handles a finished task, updating the item’s progress and adding the next operation to the queue if any.
         *
         * @param machine      The machine that finished processing.
         * @param finishedTask The task that was completed.
         */
        private void handleFinishedTask (Machine machine, Task finishedTask){
            System.out.printf("Time: %d - Machine %s has finished its task and is now available.\n", currentTime, machine.getIdMachine());

            Article processedArticle = finishedTask.getItem();
            if (processedArticle.moveToNextOperation()) {
                String nextOperation = processedArticle.getNextOperation();
                taskQueue.add(new Task(processedArticle, nextOperation));
                System.out.printf("Time: %d - Added %s of item %s to the queue (Priority: %s)\n",
                        currentTime, nextOperation, processedArticle.getIdItem(), processedArticle.getPriority());
            } else {
                updateFlowDependency(itemWorkstationHistory.get(processedArticle));
                System.out.printf("Time: %d - Item %s has completed all its operations!\n", currentTime, processedArticle.getIdItem());

            }
        }


        /**
         * Finds a machine by its ID.
         *
         * @param machineId The ID of the machine to find.
         * @return The machine with the given ID, or null if not found.
         */
        private Machine findMachineById (String machineId){
            return machines.stream()
                    .filter(machine -> machine.getIdMachine().equals(machineId))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * Updates the flow dependency map based on the workstation history of the item.
         *
         * @param workstationHistory The list of workstations where the item has been processed.
         */
        private void updateFlowDependency (List < String > workstationHistory) {
            if (workstationHistory.size() < 2) return;

            // Traverse the history and update the flow dependency between consecutive workstations
            for (int i = 0; i < workstationHistory.size() - 1; i++) {
                String from = workstationHistory.get(i);
                String to = workstationHistory.get(i + 1);

                flowDependencyMap.putIfAbsent(from, new HashMap<>());
                Map<String, Integer> transitions = flowDependencyMap.get(from);
                transitions.put(to, transitions.getOrDefault(to, 0) + 1);
            }
        }

        /**
         * Presents a report of the flow dependencies between workstations.
         * This report shows how frequently items transition from one workstation to another.
         */
        void presentWorkstationFlowReport () {
            System.out.println("\n--- Workstation Flow Dependency Report ---");

            // Sort and print the transitions for each workstation
            flowDependencyMap.keySet().stream()
                    .sorted()
                    .forEach(workstation -> {
                        List<Map.Entry<String, Integer>> sortedTransitions = flowDependencyMap.get(workstation).entrySet().stream()
                                .sorted((a, b) -> b.getValue().compareTo(a.getValue())) // Sort by the number of transitions in descending order
                                .collect(Collectors.toList());

                        String formattedTransitions = sortedTransitions.stream()
                                .map(entry -> String.format("(%s,%d)", entry.getKey(), entry.getValue())) // Format each transition
                                .collect(Collectors.joining(",")); // Join transitions with commas

                        System.out.printf("%s : [%s]\n", workstation, formattedTransitions);
                    });
        }

        /**
         * Presents a report on the utilization of each machine, including usage percentages
         * relative to the total time and to the time spent on each type of operation.
         */
        void presentMachineUtilizationReport () {
            System.out.println("\n--- Machine Usage Report (Relative to Total Usage) ---");

            double totalOperationTime = machineOperationTimes.values().stream().mapToInt(Integer::intValue).sum();

            // Map to store the total time spent on each type of operation
            Map<String, Integer> totalTimeByOperationType = new HashMap<>();

            for (Machine machine : machines) {
                // Accumulate the total operation time for each type of operation
                totalTimeByOperationType.put(machine.getOperationName(),
                        totalTimeByOperationType.getOrDefault(machine.getOperationName(), 0) + machineOperationTimes.get(machine.getIdMachine()));
            }

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

            // Display utilization for each machine
            for (MachineUtilization machineUtilization : machineUtilizations) {
                Machine machine = findMachineById(machineUtilization.getIdMachine());
                String operationName = machine != null ? machine.getOperationName() : "Unknown";
                System.out.printf("Machine: %s, Total Operation Time: %d seconds, Usage: %.2f%%, Operation Type (%s) Usage: %.2f%%\n",
                        machineUtilization.getIdMachine(), machineUtilization.getTotalTime(),
                        machineUtilization.getUtilizationPercentage(), operationName, machineUtilization.getOperationTypeUsage());
            }

            System.out.printf("\nTotal Operation Time Across All Machines: %.0f seconds\n", totalOperationTime);
        }

        /**
         * Presents a report on the average execution times for each operation and
         * the average waiting times based on the total tasks processed.
         */
        void presentOperationTimesReport () {
            System.out.println("\n--- Operation Execution and Waiting Times Report ---");

            // Display average execution time and waiting time for each operation
            for (Map.Entry<String, List<Integer>> entry : operationExecutionTimes.entrySet()) {
                String operation = entry.getKey();
                List<Integer> times = entry.getValue();
                double averageTime = times.stream().mapToInt(Integer::intValue).average().orElse(0.0);

                int totalWaitingTime = operationWaitingTimes.getOrDefault(operation, 0);
                int taskCount = operationTaskCounts.getOrDefault(operation, 1); // Prevent division by zero
                double averageWaitingTime = (double) totalWaitingTime / taskCount;

                System.out.printf("Operation: %s, Average Execution Time: %.2f units, Average Waiting Time: %.2f units\n",
                        operation, averageTime, averageWaitingTime);
            }
        }

        /**
         * Presents a report on the total time spent on each operation type and its percentage
         * relative to the total time across all operations.
         */
        void presentTotalTimePerOperation () {
            System.out.println("\n--- Total Time and Percentage Spent Per Operation ---");

            // Calculate the overall total time spent on all operations
            int overallTotalTime = operationExecutionTimes.values().stream()
                    .flatMap(List::stream)
                    .mapToInt(Integer::intValue)
                    .sum();

            // Create a sorted list of operations based on total time spent
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
         * Presents a report on the total time spent for processing each item.
         */
        void presentTotalTimePerItem () {
            System.out.println("\n--- Total Time Spent Per Item ---");

            // Create a sorted list of items based on their ID
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(totalTimePerItem.entrySet());
            sortedEntries.sort((entry1, entry2) -> Integer.compare(Integer.parseInt(entry1.getKey()), Integer.parseInt(entry2.getKey())));

            // Display total time spent for each item
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                System.out.printf("Item: %s, Total Time Spent: %d units\n", entry.getKey(), entry.getValue());
            }
        }

        /**
         * Calculates and presents a report on the total waiting time for each item.
         */
        void calculateTotalWaitingTimePerItem () {
            System.out.println("\n--- Total Waiting Time Per Item ---");

            // Create a sorted list of items based on their ID
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(itemWaitingTimes.entrySet());
            sortedEntries.sort((entry1, entry2) -> Integer.compare(Integer.parseInt(entry1.getKey()), Integer.parseInt(entry2.getKey())));

            // Display total waiting time for each item
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                System.out.printf("Item: %s, Total Waiting Time: %d units\n", entry.getKey(), entry.getValue());
            }
        }
    }


