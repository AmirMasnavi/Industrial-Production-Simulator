package org.example;

import java.util.*;

/**
 * This class represents the menu system for the application.
 * It provides methods to display options to the user and handle user input for various functionalities,
 * such as listing items, running simulations, and displaying statistics.
 */
public class MenuItem {

    private static Simulator simulator;
    private static SimulatorNoPriorities simulatorNoPriorites;
    private static boolean lastSimulationWithPriorities;
    private static BooDataResult booDataResult;

    private static SimulatorTree simulatorTree;

    /**
     * Displays the main menu and handles user selections.
     * <p>
     * The menu options allow the user to list items and machines, run simulations,
     * and view simulation statistics. It continues to prompt the user until they choose to exit.
     * </p>
     */
    static void menu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        Visualiser visualiser = new Visualiser();

        List<Item> items = CSVReader.readItemsFromCSV("./items.csv");
        List<Operation> operations = CSVReader.readOperationsFromCSV("./operations.csv");
        Map<Integer, Integer> operationToItemMap = new HashMap<>();
        booDataResult = CSVReader.readBooFromCSV("./boo_v2.csv", operationToItemMap);

        ProductionTreeBuilder treeBuilder = new ProductionTreeBuilder(items, operations, booDataResult.booData, booDataResult.itemQuantities);
        ProductionTreeNode rootNode = treeBuilder.buildTree(1006, operationToItemMap);

        ProductionTreeSearcher searcher = new ProductionTreeSearcher();
        searcher.indexTree(rootNode);

        ProductionTreeBuilderOpID treeBuilder2 = new ProductionTreeBuilderOpID(items, operations, booDataResult.booData, booDataResult.itemQuantities);
        ProductionTreeNode rootNode2 = treeBuilder2.buildTree(20, operationToItemMap);


        MaterialBST materialBST = new MaterialBST();
        for (Map.Entry<Integer, Double> entry : booDataResult.itemQuantities.entrySet()) {
            int itemId = entry.getKey();
            Double quantity = entry.getValue();

            String itemName = items.stream()
                    .filter(item -> item.getId() == itemId)
                    .map(Item::getName)
                    .findFirst()
                    .orElse("Unknown Item");

            materialBST.insert(quantity, itemName);
        }

        QualityCheckManager qualityCheckManager = new QualityCheckManager();
        qualityCheckManager.addQualityCheckBasedOnDepth(rootNode, 1);


        while (running) {
            System.out.println("\n=== MENU ===\n");
            System.out.println("1. List Items");
            System.out.println("2. List Available Machines");
            System.out.println("3. Run Simulation");
            System.out.println("4. Run Simulation With Priorities");
            System.out.println("5. Show Simulation Statistics");
            System.out.println("6. Build Production Tree");
            System.out.println("7. Search for Specific Operation or Material");
            System.out.println("8. Display Materials by Quantity");
            System.out.println("9. Perform Quality Checks by Priority");
            System.out.println("10. Update Material Quantity");
            System.out.println("11. Total Material Quantity");
            System.out.println("12. Total Material Quantity.2");
            System.out.println("13. Critical Path Operation");
            System.out.println("14. List Products and View BOM or BOO (LAPR3)");
            System.out.println("15. Run Simulation Tree");

            System.out.println("0. Exit");

            System.out.print("\nChoose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    listItems();
                    break;
                case 2:
                    listMachines();
                    break;
                case 3:
                    runSimulationWithoutPriorities();
                    break;
                case 4:
                    runSimulation();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 6:
                    productionTree();
                    break;
                case 7:
                    searchOperationOrMaterial(searcher);
                    break;
                case 8:
                    displayMaterialsByQuantity(materialBST);
                    break;
                case 9:
                    performQualityChecks(qualityCheckManager);
                    break;
                case 10:
                    updateMaterialQuantity(searcher, materialBST);
                    break;
                case 11:
                    displayTotalMaterials(rootNode);
                    break;
                case 12:
                    displayTotalMaterials2(materialBST);
                    break;
                case 13:
                    criticalPathOperations();
                    break;
                case 14:
                    listAndShowProducts(visualiser);
                    break;
                case 15:
                    runSimulationTree();
                case 0:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Lists and shows available products to the user.
     * <p>
     * This method allows the user to choose a product from a list and then view its corresponding Bill of Materials (BOM)
     * or Bill of Operations (BOO). It provides options for users to exit or go back after selecting a product.
     * </p>
     *
     * @param visualiser the visualizer instance used to list products and display BOM/BOO.
     */

    private static void listAndShowProducts(Visualiser visualiser) {
        Scanner scanner = new Scanner(System.in);

        // List all available products
        visualiser.listProducts();

        System.out.print("Choose a product by entering its number: ");
        int selectedProductIndex = scanner.nextInt();

        // Check if the selected product index is valid
        if (selectedProductIndex < 1 || selectedProductIndex > visualiser.products.size()) {
            System.out.println("Invalid product number. Please try again.");
            return;
        }

        // Retrieve the selected product
        Product selectedProduct = visualiser.products.get(selectedProductIndex - 1);

        System.out.println("\nWhat would you like to view?");
        System.out.println("1. Bill of Materials (BOM)");
        System.out.println("2. Bill of Operations (BOO)");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                // Print the BOM for the selected product
                visualiser.printBOM(selectedProduct);
                break;
            case 2:
                // Print the BOO for the selected product
                visualiser.printBOO(selectedProduct);
                break;
            case 3:
                System.out.println("Exiting...");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    /**
     * Lists the items available in the system, allowing the user to filter by priority.
     * <p>
     * This method prompts the user to select a priority filter and displays the corresponding items.
     * It continues to prompt the user until they choose to go back to the main menu.
     * </p>
     */
    private static void listItems() {
        Scanner scanner = new Scanner(System.in);
        List<Article> originalArticles = CSVReader.readArticlesFromCSV("./articles.csv");

        boolean backToMenu = false;

        while (!backToMenu) {

            List<Article> articles = new ArrayList<>(originalArticles);

            System.out.println("\n=== Select Priority ===");
            System.out.println("1. List all items (ordered by priority)");
            System.out.println("2. List items with priority LOW");
            System.out.println("3. List items with priority NORMAL");
            System.out.println("4. List items with priority HIGH");
            System.out.println("0. Go back to Menu");

            System.out.print("\nChoose an option: ");
            int priorityOption = scanner.nextInt();

            switch (priorityOption) {
                case 1:
                    articles.sort((i1, i2) -> {
                        int p1 = new Task(i1, i1.getNextOperation()).getPriority();
                        int p2 = new Task(i2, i2.getNextOperation()).getPriority();
                        return Integer.compare(p2, p1); // Ordena por prioridade decrescente
                    });
                    break;
                case 2:
                    articles = articles.stream()
                            .filter(item -> item.getPriority() == Article.Priority.LOW)
                            .toList();
                    break;
                case 3:
                    articles = articles.stream()
                            .filter(item -> item.getPriority() == Article.Priority.NORMAL)
                            .toList();
                    break;
                case 4:
                    articles = articles.stream()
                            .filter(item -> item.getPriority() == Article.Priority.HIGH)
                            .toList();
                    break;
                case 0:
                    backToMenu = true;
                    continue;
                default:
                    System.out.println("Invalid option.");
                    continue;
            }

            System.out.println("\n=== List of Items ===");
            if (articles.isEmpty()) {
                System.out.println("No items found for the selected priority.");
            } else {
                for (Article article : articles) {
                    System.out.println(article);
                }
            }
        }
    }


    /**
     * Lists the available machines in the system.
     * <p>
     * This method retrieves and displays the machines read from a CSV file.
     * It continues to prompt the user until they choose to go back to the main menu.
     * </p>
     */
    private static void listMachines() {
        Scanner scanner = new Scanner(System.in);
        List<Machine> machines = CSVReader.readMachinesFromCSV("./workstations.csv");

        boolean backToMenu = false;

        while (!backToMenu) {
            System.out.println("\n=== List of Machines ===");
            for (Machine machine : machines) {
                System.out.println(machine);
            }

            System.out.println("\nPress 0 to go back to the Menu.");
            int backOption = scanner.nextInt();
            if (backOption == 0) {
                backToMenu = true;
            }
        }
    }

    /**
     * Runs a simulation without considering item priorities.
     * <p>
     * This method initializes the simulator with items and machines read from CSV files
     * and executes the simulation.
     * </p>
     */
    private static void runSimulation() {
        List<Article> articles = CSVReader.readArticlesFromCSV("./articles.csv");
        List<Machine> machines = CSVReader.readMachinesFromCSV("./workstations.csv");

        simulator = new Simulator(articles, machines);
        simulator.runSimulation();
        lastSimulationWithPriorities = true;
        System.out.println("\nSimulation with priorities completed.");
    }

    /**
     * Runs a simulation considering item priorities.
     * <p>
     * This method initializes the simulator without priorities with items and machines read from CSV files
     * and executes the simulation.
     * </p>
     */
    private static void runSimulationWithoutPriorities() {
        List<Article> articles = CSVReader.readArticlesFromCSV("./articles.csv");
        List<Machine> machines = CSVReader.readMachinesFromCSV("./workstations.csv");

        simulatorNoPriorites = new SimulatorNoPriorities(articles, machines);
        simulatorNoPriorites.runSimulation();
        lastSimulationWithPriorities = false;
        System.out.println("\nSimulation without priorities completed.");
    }

    /**
     * Displays statistics from the last simulation run.
     * <p>
     * This method checks the type of the last simulation (with or without priorities)
     * and calls the appropriate method to show statistics.
     * </p>
     */
    private static void showStatistics() {
        if (lastSimulationWithPriorities && simulator != null) {
            showStatisticsWithPriorities();
        } else if (!lastSimulationWithPriorities && simulatorNoPriorites != null) {
            showStatisticsWithoutPriorities();
        } else {
            System.out.println("No simulation has been run yet. Please run a simulation first.");
        }
    }


    /**
     * Builds and displays the production tree based on the provided data.
     * <p>
     * This method loads the items, operations, and BOO data from CSV files, constructs two
     * different production trees, prints both trees, and indexes them for searching. It does not
     * allow direct user interaction.
     * </p>
     */
    private static void productionTree() {
        // Load data from CSV files
        List<Item> items = CSVReader.readItemsFromCSV("./items.csv");
        List<Operation> operations = CSVReader.readOperationsFromCSV("./operations.csv");

        // Create a map to store the mapping between op_id and item_id
        Map<Integer, Integer> operationToItemMap = new HashMap<>();
        BooDataResult booDataResult = CSVReader.readBooFromCSV("./boo_v2.csv", operationToItemMap);

        // Create a ProductionTreeBuilder with the read data
        ProductionTreeBuilder treeBuilder = new ProductionTreeBuilder(items, operations, booDataResult.booData, booDataResult.itemQuantities);

        // Create a scanner for user input
        Scanner scanner = new Scanner(System.in);
        int itemId = -1;

        // Loop until a valid item ID is provided
        while (true) {
            System.out.print("Enter the ID of the item to build the production tree: ");
            try {
                itemId = Integer.parseInt(scanner.nextLine());
                // Check if the item ID exists in the items list
                int finalItemId = itemId;
                boolean itemExists = items.stream().anyMatch(item -> item.getId() == finalItemId);

                if (itemExists) {
                    break; // Valid item ID found, exit the loop
                } else {
                    System.out.println("Invalid item ID. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid numeric ID.");
            }
        }

        // Build the production tree for the specified item ID
        ProductionTreeNode rootNode = treeBuilder.buildTree(itemId, operationToItemMap);

        // Print the production tree
        ProductionTreePrinter printer = new ProductionTreePrinter(booDataResult.booData);
        printer.printTree(rootNode);

        // Create and use the searcher
        ProductionTreeSearcher searcher = new ProductionTreeSearcher();
        searcher.indexTree(rootNode); // Index the tree for searching

    }


    /**
     * Displays statistics for the last simulation that considered item priorities.
     * <p>
     * This method allows the user to select from various statistical reports related to the simulation with priorities.
     * It continues to prompt the user until they choose to go back to the main menu.
     * </p>
     */
    private static void showStatisticsWithPriorities() {
        Scanner scanner = new Scanner(System.in);
        boolean backToMenu = false;

        while (!backToMenu) {
            System.out.println("\n=== Simulation Statistics (With Priorities) ===");
            System.out.println("1. Show Total Time Spent Per Item");
            System.out.println("2. Show Total Time Spent Per Operation");
            System.out.println("3. Show Machine Utilization Report");
            System.out.println("4. Show Operation Execution and Waiting Times Report");
            System.out.println("5. Show Workstation Flow Dependency Report");
            System.out.println("6. Show Total Waiting Time Per Item");
            System.out.println("0. Go back to Menu");
            System.out.print("\nChoose an option: ");

            int reportOption = scanner.nextInt();

            switch (reportOption) {
                case 1:
                    simulator.presentTotalTimePerItem();
                    break;
                case 2:
                    simulator.presentTotalTimePerOperation();
                    break;
                case 3:
                    simulator.presentMachineUtilizationReport();
                    break;
                case 4:
                    simulator.presentOperationTimesReport();
                    break;
                case 5:
                    simulator.presentWorkstationFlowReport();
                    break;
                case 6:
                    simulator.calculateTotalWaitingTimePerItem();
                case 0:
                    backToMenu = true;
                    continue;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 5.");
                    continue;
            }
        }
    }

    /**
     * Searches for a specific operation or material by name or ID.
     * <p>
     * This method allows the user to search for a material or operation based on the user input.
     * The search result is then printed to the console.
     * </p>
     * @param searcher The searcher object used for searching the production tree.
     */
    private static void searchOperationOrMaterial(ProductionTreeSearcher searcher) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name or ID of the operation/material to search:");
        String searchQuery = scanner.nextLine();

        // Execute search and display the formatted output
        String result = searcher.search(searchQuery);
        System.out.println(result);
    }



    /**
     * Displays materials sorted by their quantity in either increasing or decreasing order.
     * <p>
     * This method prompts the user to select the desired order of display for materials based
     * on their quantity. The materials are displayed in increasing or decreasing order as per the user's choice.
     * </p>
     * @param materialBST The Material Binary Search Tree (BST) used for storing and displaying materials.
     */
    private static void displayMaterialsByQuantity(MaterialBST materialBST) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nChoose the order to display materials:");
        System.out.println("1. Increasing order of quantity");
        System.out.println("2. Decreasing order of quantity");

        int orderChoice = scanner.nextInt();

        if (orderChoice == 1) {
            System.out.println("\nMaterials Sorted by Quantity (Increasing Order):");
            materialBST.displayInOrder();
        } else if (orderChoice == 2) {
            System.out.println("\nMaterials Sorted by Quantity (Decreasing Order):");
            materialBST.displayInReverseOrder();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /**
     * Displays the total quantity of materials used in the production process.
     * <p>
     * This method calculates and displays the total material quantities used in the production process
     * by traversing the production tree node.
     * </p>
     * @param node The root node of the production tree used to calculate and display total material quantities.
     */
    private static void displayTotalMaterials(ProductionTreeNode node) {
        System.out.println("\nTotal Quantity of Materials Used: ");
        node.displayTotalMaterials(node);
    }

    /**
     * Displays the total quantity of materials used in the material BST.
     * <p>
     * This method calculates and displays the total material quantities stored in the MaterialBST.
     * </p>
     * @param materialBST The Material Binary Search Tree (BST) used to store and calculate material quantities.
     */
    private static void displayTotalMaterials2(MaterialBST materialBST){
        System.out.println("\nTotal Quantity of Materials Used: ");
        materialBST.displayTotalMaterialsTest();
    }


    /**
     * Performs quality checks in reverse priority order.
     * <p>
     * This method triggers the quality check manager to perform quality checks based on the
     * priority order in reverse (highest priority first).
     * </p>
     * @param qualityCheckManager The quality check manager used to process quality checks.
     */
    private static void performQualityChecks(QualityCheckManager qualityCheckManager) {
        System.out.println("\nPerforming Quality Checks in Priority Order:");
        qualityCheckManager.processQualityChecksInReverse();
    }

    /**
     * Allows the user to update the quantity of a specific material in both the production tree and the material BST.
     * <p>
     * This method prompts the user to enter the name or ID of a material, and then allows them
     * to update its quantity. The updated quantity is reflected in both the production tree and the material BST.
     * </p>
     * @param searcher The searcher used to find the material in the production tree.
     * @param materialBST The Material Binary Search Tree (BST) used to update the material quantity.
     */
    private static void updateMaterialQuantity(ProductionTreeSearcher searcher, MaterialBST materialBST) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name or ID of the material to update:");
        String searchQuery = scanner.nextLine();

        ProductionTreeNode node = searcher.getNodeByNameOrId(searchQuery);

        if (node != null) {

            System.out.println("Enter the new quantity:");
            double newQuantity = scanner.nextDouble();


            node.updateMaterialQuantity(newQuantity);
            booDataResult.updateItemQuantity(node.getItem().getId(), newQuantity);
            System.out.println("Material quantity updated in the production tree.");

            materialBST.updateMaterialQuantity(searchQuery, newQuantity);
            System.out.println("Material quantity updated in the material BST.");
        } else {
            System.out.println("Error: Material not found in the production tree.");
        }
    }

    /**
     * Displays the critical path operations based on item and operation data.
     * <p>
     * This method loads the necessary data and then calls a method to display the critical path operations
     * in the production process.
     * </p>
     */
    private static void criticalPathOperations() {
        List<Item> items = CSVReader.readItemsFromCSV("./items.csv");
        List<Operation> operations = CSVReader.readOperationsFromCSV("./operations.csv");
        Map<Integer, Integer> operationToItemMap = new HashMap<>();
        BooDataResult booDataResult = CSVReader.readBooFromCSV("./boo_v2.csv", operationToItemMap);

        criticalPathOperations(items, operations, booDataResult);
    }

    private static void criticalPathOperations(List<Item> items, List<Operation> operations, BooDataResult booDataResult) {
        CriticalPathOperations.criticalPathOperations(items, operations, booDataResult);
    }

    /**
     * Displays statistics for the last simulation that did not consider item priorities.
     * <p>
     * This method allows the user to select from various statistical reports related to the simulation without priorities.
     * It continues to prompt the user until they choose to go back to the main menu.
     * </p>
     */
    private static void showStatisticsWithoutPriorities() {
        Scanner scanner = new Scanner(System.in);
        boolean backToMenu = false;

        while (!backToMenu) {
            System.out.println("\n=== Simulation Statistics (Without Priorities) ===");
            System.out.println("1. Show Total Time Spent Per Item");
            System.out.println("2. Show Total Time Spent Per Operation");
            System.out.println("3. Show Machine Utilization Report");
            System.out.println("4. Show Operation Execution and Waiting Times Report");
            System.out.println("5. Show Workstation Flow Dependency Report");
            System.out.println("6. Show Total Waiting Time Per Item");
            System.out.println("0. Go back to Menu");
            System.out.print("\nChoose an option: ");

            int reportOption = scanner.nextInt();

            switch (reportOption) {
                case 1:
                    simulatorNoPriorites.presentTotalTimePerItem();
                    break;
                case 2:
                    simulatorNoPriorites.presentTotalTimePerOperation();
                    break;
                case 3:
                    simulatorNoPriorites.presentMachineUtilizationReport();
                    break;
                case 4:
                    simulatorNoPriorites.presentOperationTimesReport();
                    break;
                case 5:
                    simulatorNoPriorites.presentWorkstationFlowReport();
                    break;
                case 6:
                    simulatorNoPriorites.presentTotalWaitTimePerItem();
                    break;
                case 0:
                    backToMenu = true;
                    continue;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 5.");
                    continue;
            }
        }
    }

    /**
     * Runs the simulation using the specified CSV files for items, operations, BOO data, and workstations.
     * <p>
     * This method reads the necessary input CSV files, creates an instance of the simulation
     * system, and then executes the simulation. It handles any exceptions that may arise
     * during the simulation process and prints the results to the console.
     * </p>
     */
    private static void runSimulationTree() {
        try {
            // Ler os ficheiros CSV necessários
            String itemsFilePath = "./items.csv";
            String operationsFilePath = "./operations.csv";
            String booFilePath = "./boo_v2.csv";
            String machinesFilePath = "./workstations_v2.csv";

            // Criar a instância do simulador com base nos ficheiros
            SimulatorTree simulator = new SimulatorTree(
                    itemsFilePath, operationsFilePath, booFilePath, machinesFilePath
            );

            // Executar a simulação
            simulator.runSimulation();
            System.out.println("\nSimulation Tree completed.");
        } catch (Exception e) {
            System.err.println("Erro ao executar a simulação: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
