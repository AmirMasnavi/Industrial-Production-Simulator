package org.example;

import com.opencsv.CSVWriter;
import oracle.jdbc.internal.OracleTypes;
import org.example.sprint3.*;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.Article.Priority.*;
import static org.example.sprint3.Activity.findActivityById;

/**
 * This class represents the menu system for the application.
 * It provides methods to display options to the user and handle user input for various functionalities,
 * such as listing items, running simulations, and displaying statistics.
 */
public class MenuItem {

    static Simulator simulator;
    private static SimulatorNoPriorities simulatorNoPriorites;
    private static boolean lastSimulationWithPriorities;
    private static BooDataResult booDataResult;
    private static final PERTCPMGraph pertcpmGraph = new PERTCPMGraph(); // Single instance of PERTCPMGraph
    private static List<Activity> activities; // List to store activities

    static void menu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        Visualiser visualiser = new Visualiser();

        List<Article> articles = CSVReader.readArticlesFromCSV("./orderArticles.csv");
        List<Machine> machines = CSVReader.readMachinesFromCSV("./orderMachines.csv");
        List<Item> items = CSVReader.readItemsFromCSV("./items.csv");
        List<Operation> operations = CSVReader.readOperationsFromCSV("./operations.csv");
        Map<Integer, Integer> operationToItemMap = new HashMap<>();
        booDataResult = CSVReader.readBooFromCSV("./boo_v2.csv", operationToItemMap);

        ProductionTreeBuilder treeBuilder = new ProductionTreeBuilder(items, operations, booDataResult.booData, booDataResult.itemQuantities);
        ProductionTreeNode rootNode = treeBuilder.buildTree(1006, operationToItemMap);

        ProductionTreeSearcher searcher = new ProductionTreeSearcher();
        searcher.indexTree(rootNode);

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

        // Read activities from CSV file at the beginning
        activities = CSVReader.readActivitiesFromCsv("./small_project.csv");

        while (running) {
            System.out.println("\n" + "═".repeat(40));
            System.out.println("🎯  \u001B[1mPRODUCTION SYSTEM MENU\u001B[0m  🎯");
            System.out.println("═".repeat(40) + "\n");

            System.out.println("\u001B[36m🔧 Production Simulation\u001B[0m");
            System.out.println("🔹 1. List Items 🗂️");
            System.out.println("🔹 2. List Available Machines 🏭");
            System.out.println("🔹 3. Run Simulation 🎮");
            System.out.println("🔹 4. Run Simulation With Priorities ⚙️");
            System.out.println("🔹 5. Show Simulation Statistics 📊");

            System.out.println("\n\u001B[36m🌳 Production Tree\u001B[0m");
            System.out.println("🔹 6. Build Production Tree 🌳");
            System.out.println("🔹 7. Search for Operation or Material 🔍");
            System.out.println("🔹 8. Display Materials by Quantity 📦");
            System.out.println("🔹 9. Perform Quality Checks by Priority ✅");
            System.out.println("🔹 10. Update Material Quantity 🛠️");
            System.out.println("🔹 11. Total Material Quantity 📈");
            System.out.println("🔹 12. Total Material Quantity.2 📉");
            System.out.println("🔹 13. Critical Path Operation 🚀");
            System.out.println("🔹 14. Run Simulation Tree 🧬");

            System.out.println("\n\u001B[36m📦 Product BOM/BOO Management\u001B[0m");
            System.out.println("🔹 15. List Products and BOM/BOO (LAPR3) 🛒");

            System.out.println("\n\u001B[31m🚩 Project Management Options\u001B[0m");
            System.out.println("🔹 16. Build PERT-CPM Graph 🖋️");
            System.out.println("🔹 17. Detect Circular Dependencies 🔄");
            System.out.println("🔹 18. Activities Topological Sort 🗺️");
            System.out.println("🔹 19. Calculate Earliest and Latest Times ⏳");
            System.out.println("🔹 20. Export Project Schedule to CSV 📤");
            System.out.println("🔹 21. Identify Critical Path 📍");
            System.out.println("🔹 22. Identify Bottleneck Activities 📌");
            System.out.println("🔹 23. Simulate Project Delays ⌛");

            System.out.println("\n\u001B[36m📂 Order Simulation\u001B[0m");
            System.out.println("🔹 24. Simulate Order from Oracle SGBD 📂");



            System.out.println("\n\u001B[31m0️⃣  Exit\u001B[0m 🚪");


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
                    runSimulationWithoutPriorities(articles, machines);
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
                    runSimulationTree();
                    break;
                case 15:
                    listAndShowProducts(visualiser);
                    break;
                case 16:
                    buildPertCpmGraph();
                    break;
                case 17:
                    validateNoCircularDependencies();
                    break;
                case 18:
                    performTopologicalSort();
                    break;
                case 19:
                    calculateEarliestAndLatestTimes();
                    break;
                case 20:
                    exportScheduleToCsv();
                    break;
                case 21:
                    identifyCriticalPath();
                    break;
                case 22:
                    identifyBottleneckActivities();
                    break;
                case 23:
                    simulateProjectDelaysMenu();
                    break;
                case 24:
                    openNewSimulator();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void openNewSimulator() throws SQLException {
        Scanner sc = new Scanner(System.in);  // Create a scanner for user input
        System.out.println("\n" + "═".repeat(40));  // Print a line for separation
        System.out.println("🎯  \u001B[1mLAPR SIMULATION MENU\u001B[0m  🎯");  // Display the simulation menu title
        System.out.println("═".repeat(40) + "\n");  // Print another line for separation

        // Display menu options
        System.out.println("🔹 1. Run Simulation with csv file 📂");
        System.out.println("🔹 2. Place an order manually and run simulation ✍️");
        System.out.println("🔹 3. Print AVL tree 🌴");
        System.out.println("\n\u001B[31m0️⃣  Return to main menu\u001B[0m 🚪");

        int option;  // Variable to hold the user's choice

        // Prompt the user to choose an option
        System.out.println("Choose an option: ");
        option = sc.nextInt();  // Read the user's input

        // Switch case to handle different options based on user choice
        switch(option){
            case 1:
                // Run simulation based on the CSV file
                USLP06.simulateProduction();
                break;
            case 2:
                // Handle manual order placement and simulation with improved interface
                List<Machine> machines = CSVReader.readMachinesFromCSV("./orderMachines.csv");  // Read available machines from CSV

                // Clear and structured display of available products
                System.out.println("\n" + "═".repeat(40));  // Print a separator line
                System.out.println("🎯  \u001B[1mAvailable Products for Order\u001B[0m");
                System.out.println("═".repeat(40) + "\n");

                // Display the list of available products with bullet points for clarity
                System.out.println("🔹 AS12945T22");
                System.out.println("🔹 AS12945S22");
                System.out.println("🔹 AS12945S20");
                System.out.println("🔹 AS12945S17");
                System.out.println("🔹 AS12945P17");
                System.out.println("🔹 AS12945S48");
                System.out.println("🔹 AS12945G48");
                System.out.println("🔹 AS12946S22");
                System.out.println("🔹 AS12947S22");
                System.out.println("🔹 AS12946S20");
                System.out.println();  // Add some space for readability

                // Prompt user to choose a product
                System.out.println("\u001B[1mChoose the Product ID from the list above:\u001B[0m");
                String productID = sc.next();  // Read the product ID chosen by the user

                // Display priority selection instructions
                System.out.println("\n\u001B[1mSelect Priority Level:\u001B[0m");
                System.out.println("🔹 1. High");
                System.out.println("🔹 2. Normal");
                System.out.println("🔹 3. Low");
                System.out.println("\n\u001B[1mChoose a priority number:\u001B[0m");
                int priorityOption = sc.nextInt();  // Read the priority choice from the user
                Article.Priority priority = null;

                // Set the priority based on the user's choice
                switch(priorityOption){
                    case 1:
                        priority = Article.Priority.HIGH;
                        break;
                    case 2:
                        priority = Article.Priority.NORMAL;
                        break;
                    case 3:
                        priority = Article.Priority.LOW;
                        break;
                    default:
                        System.out.println("\u001B[31mInvalid selection! Defaulting to Normal priority.\u001B[0m");
                        priority = Article.Priority.NORMAL;
                        break;
                }

                // Ask for the number of operations required with a better layout
                System.out.println("\n\u001B[1mHow many operations do you need?\u001B[0m");
                System.out.println("\n⚠️ The maximum allowed is 6 operations.");
                List<String> operations = new ArrayList<>();
                int numOfOper = sc.nextInt();  // Read the number of operations

                // Check if the number of operations is within the valid range
                if(numOfOper <= 6 && numOfOper >= 1){
                    // Loop through to collect the operations
                    for (int i = 0; i < numOfOper; i++) {
                        System.out.println("\n\u001B[1mAvailable Operations (Choose by ID):\u001B[0m");
                        System.out.println("🔹 5647");
                        System.out.println("🔹 5649");
                        System.out.println("🔹 5651");
                        System.out.println("🔹 5653");
                        System.out.println("🔹 5659");
                        System.out.println("🔹 5655");
                        System.out.println("🔹 5657");
                        System.out.println("🔹 5661");
                        System.out.println("🔹 5667");
                        System.out.println("🔹 5663");
                        System.out.println("\n\u001B[1mChoose operation ID for Operation " + (i + 1) + ":\u001B[0m");
                        String oper = sc.next();  // Read the operation ID from the user
                        operations.add(oper);  // Add the operation to the list
                    }
                } else if (numOfOper < 1) {
                    System.out.println("\u001B[31m⚠️ Invalid number! Operations must be greater than 1.\u001B[0m");
                } else if (numOfOper > 6) {
                    System.out.println("\u001B[31m⚠️ The maximum number of operations is 6.\u001B[0m");
                } else {
                    System.out.println("\u001B[31m⚠️ Please enter a valid number between 1 and 6.\u001B[0m");
                }

                // Ask the user for the quantity of products
                System.out.println("\n\u001B[1mHow many units of the selected product would you like to order?\u001B[0m");
                System.out.print("Type a number: ");
                int quantity = sc.nextInt();  // Read the quantity from the user

                // Create a list of articles based on the user's input
                List<Article> articles = new ArrayList<>();
                Article article = new Article(productID, priority, operations);
                for (int i = 0; i < quantity; i++) {
                    articles.add(article);  // Add the article to the list for the specified quantity
                }

                // Simulate the production with the user's orders
                Simulator simulator = new Simulator(articles, machines, DatabaseConnection.createConnection());
                simulator.runSimulation();
                break;

            case 3:
                // Print the AVL tree of orders
                try {
                    List<Order> orders = CSVReader.readOrdersFromCSV("./orders.csv");  // Read orders from CSV file
                    buildAndPrintBalancedTrees(orders);  // Call method to build and print the AVL tree
                } catch (SQLException e) {
                    // Handle SQL exceptions and print detailed error message
                    System.err.println("SQL Error: " + e.getMessage());
                    e.printStackTrace();  // Print stack trace for debugging
                } catch (Exception e) {
                    // Handle any other unexpected exceptions
                    System.err.println("Unexpected Error: " + e.getMessage());
                    e.printStackTrace();  // Print stack trace for debugging
                }
                break;

            case 0:
                // Return to the main menu if option 0 is selected
                menu();
        }
    }

    /**
     * This method retrieves the subparts (components) of a product from the database.
     * It executes a database function that returns a list of subparts used in the product.
     * @param productId The product ID for which the subparts are to be fetched.
     * @param connection The database connection object.
     * @return A map containing the part numbers and their quantities.
     * @throws SQLException If there is an error in querying the database.
     */
    public static Map<String, Double> getSubparts(String productId, DatabaseConnection connection) throws SQLException {
        Map<String, Double> subparts = new HashMap<>();  // Create a map to store subparts

        // Prepare the callable statement to execute the function in the database
        String functionCall = "{ ? = call list_parts_used_product(?) }";
        try (CallableStatement stmt = connection.getInternalConnection().prepareCall(functionCall)) {
            stmt.registerOutParameter(1, OracleTypes.CURSOR);  // Register the output cursor
            stmt.setString(2, productId);  // Set the input parameter (product ID)

            stmt.execute();  // Execute the stored function

            // Retrieve the cursor result set from the function's output
            ResultSet rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                String partNumber = rs.getString("PartNumber");  // Get the part number
                double quantity = rs.getDouble("Quantity");  // Get the quantity of the part
                subparts.put(partNumber, quantity);  // Add the part and its quantity to the map
            }
        }

        return subparts;  // Return the map of subparts
    }

    /**
     * This method builds and prints a balanced tree of orders, where each order contains product subparts.
     * It creates a tree structure to organize the products and their components.
     * @param orders A list of orders to be processed.
     * @throws SQLException If there is an error in querying the database.
     */
    public static void buildAndPrintBalancedTrees(List<Order> orders) throws SQLException {
        DatabaseConnection connection = DatabaseConnection.createConnection();  // Create a database connection
        BalancedTree tree = new BalancedTree();  // Create a balanced tree object

        try {
            for (Order order : orders) {
                String productId = order.getProductId();  // Get the product ID from the order
                Map<String, Double> subparts = getSubparts(productId, connection);  // Get the subparts for the product

                // Insert the product node into the tree
                tree.addNode(productId, productId, subparts);

                // Insert the subparts into the tree
                for (Map.Entry<String, Double> entry : subparts.entrySet()) {
                    String partId = entry.getKey();  // Get the part ID
                    double quantity = entry.getValue();  // Get the quantity of the part
                    tree.addNode(partId, productId, subparts);  // Add the part to the tree
                }
            }

            // Print the entire tree
            System.out.println("Árvore de Subcomponentes do Produto (Unificada):");
            tree.traverseInOrder();  // Traverse the tree in order and print its content
        } finally {
            connection.close();  // Ensure the database connection is closed after use
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

            System.out.println("\n" + "═".repeat(40));
            System.out.println("🔍  \u001B[1mSELECT PRIORITY MENU\u001B[0m");
            System.out.println("═".repeat(40) + "\n");

            System.out.println("🔹 1. List all items (ordered by priority) 🗂️");
            System.out.println("🔹 2. List items with priority \u001B[34mLOW\u001B[0m ⬇️");
            System.out.println("🔹 3. List items with priority \u001B[33mNORMAL\u001B[0m ➖");
            System.out.println("🔹 4. List items with priority \u001B[31mHIGH\u001B[0m 🔺");
            System.out.println("\n\u001B[31m0️⃣  Go back to Menu\u001B[0m 🚪");


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
                            .filter(item -> item.getPriority() == LOW)
                            .toList();
                    break;
                case 3:
                    articles = articles.stream()
                            .filter(item -> item.getPriority() == NORMAL)
                            .toList();
                    break;
                case 4:
                    articles = articles.stream()
                            .filter(item -> item.getPriority() == HIGH)
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
    private static void runSimulation() throws SQLException {
        List<Article> articles = CSVReader.readArticlesFromCSV("./articles.csv");
        List<Machine> machines = CSVReader.readMachinesFromCSV("./workstations.csv");
        DatabaseConnection dbConnection = new DatabaseConnection();

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
    private static void runSimulationWithoutPriorities(List<Article> articles, List<Machine> machines) {
        simulatorNoPriorites = new SimulatorNoPriorities(articles, machines);
        simulatorNoPriorites.runSimulation();
        lastSimulationWithPriorities = false;
        System.out.println("\nSimulation Completed.");
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
        int itemId;

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
            printStatisticsMenu("Simulation Statistics (With Priorities)");
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
                    break;
                case 0:
                    backToMenu = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 6.");
            }
        }
    }


    /**
     * Auxiliary method to prevent repeated code on both Simulators Statistics Menu
     */
    private static void printStatisticsMenu(String title) {
        System.out.println("\n" + "═".repeat(40));
        System.out.println("📊  \u001B[1m" + title.toUpperCase() + "\u001B[0m");
        System.out.println("═".repeat(40) + "\n");

        System.out.println("🔹 1. Show Total Time Spent Per Item ⏱️");
        System.out.println("🔹 2. Show Total Time Spent Per Operation ⚙️");
        System.out.println("🔹 3. Show Machine Utilization Report 🏭");
        System.out.println("🔹 4. Show Operation Execution and Waiting Times Report 📈");
        System.out.println("🔹 5. Show Workstation Flow Dependency Report 🔗");
        System.out.println("🔹 6. Show Total Waiting Time Per Item ⌛");
        System.out.println("\n\u001B[31m0️⃣  Go back to Menu\u001B[0m 🚪");

        System.out.print("\nChoose an option: ");
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
        System.out.println("\n" + "═".repeat(40));
        System.out.println("📦  \u001B[1mMATERIAL DISPLAY ORDER\u001B[0m");
        System.out.println("═".repeat(40) + "\n");

        System.out.println("🔹 1. Increasing order of quantity 📈");
        System.out.println("🔹 2. Decreasing order of quantity 📉");


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
        // Create a map to store the mapping between op_id and item_id
        Map<Integer, Integer> operationToItemMap = new HashMap<>();
        BooDataResult booDataResult = CSVReader.readBooFromCSV("./boo_v2.csv", operationToItemMap);
        ProductionTreeBuilderOpID treeBuilder = new ProductionTreeBuilderOpID(items, operations, booDataResult.booData, booDataResult.itemQuantities);
        // Map to store operations and their respective tree depths
        Map<Operation, Integer> operationDepths = new HashMap<>();
        // Calculate the depth of each tree
        for (Operation operation : operations) {
            ProductionTreeNode rootNode = treeBuilder.buildTree(operation.getId(), operationToItemMap);
            int depth = calculateTreeDepth(rootNode);
            operationDepths.put(operation, depth);
        }
        // Sort operations by tree depth in descending order
        List<Operation> sortedOperations = operations.stream()
                .sorted((op1, op2) -> Integer.compare(operationDepths.get(op2), operationDepths.get(op1)))
                .toList();
        // Print the trees in descending order of depth
        ProductionTreePrinter printer = new ProductionTreePrinter(booDataResult.booData);
        for (Operation operation : sortedOperations) {
            System.out.println("\nCritical Path Operation for: " + operation.getName() + " (ID: " + operation.getId() + ")");
            ProductionTreeNode rootNode = treeBuilder.buildTree(operation.getId(), operationToItemMap);
            printer.printOperationTree(rootNode);
        }
    }

    /**
     *Calculates Tree Depth for a given node recursively and returns the depth.
     * @param node The root node of the production tree.
     */
    private static int calculateTreeDepth(ProductionTreeNode node) {
        if (node == null || node.getChildren().isEmpty()) {
            return 1; // Base case: a single node has depth 1
        }
        // Recursively calculate the depth of children and add 1 for the current node
        int maxDepth = 0;
        for (ProductionTreeNode child : node.getChildren()) {
            maxDepth = Math.max(maxDepth, calculateTreeDepth(child));
        }
        return maxDepth + 1;
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
            printStatisticsMenu("Simulation Statistics (Without Priorities)");
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
                    break;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 6.");
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
        List<Article> new_articles;
        List<Machine> new_machines;

        new_articles = CSVReader.readArticlesFromCSV("./bench.csv");
        new_machines = CSVReader.readMachinesFromCSV("./workstations_v2.csv");

        AVLTree avlTree = new AVLTree();

        // Populate the AVL tree with operations and their dependencies
        booDataResult.booData.forEach((itemId, subcomponents) -> {
            // For each item produced, we will insert an operation into the AVL tree
            for (Map.Entry<Integer, Double> entry : subcomponents.entrySet()) {
                int opId = entry.getKey();
                avlTree.insert(opId, itemId, subcomponents);
            }
        });

        // Traverse the AVL tree and simulate the production process
        avlTree.inorderTraversal();

        runSimulationWithoutPriorities(new_articles, new_machines);
    }

    /**
     * Builds the PERT-CPM graph based on the selected project file.
     * Prompts the user to choose between predefined project files and
     * constructs a graph using the activities defined in the selected file.
     * Displays graph details, including the number of nodes, edges, and their attributes.
     */
    private static void buildPertCpmGraph() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n" + "═".repeat(40));
        System.out.println("📐  \u001B[1mBUILD PERT-CPM GRAPH\u001B[0m");
        System.out.println("═".repeat(40) + "\n");

        System.out.println("🔹 1. small_project.csv 📄");
        System.out.println("🔹 2. large_project.csv 📄");
        System.out.print("\nChoose an option: ");

        int choice = scanner.nextInt();

        String filePath;
        if (choice == 1) {
            filePath = "./small_project.csv";
        } else if (choice == 2) {
            filePath = "./large_project.csv";
        } else {
            System.out.println("Invalid choice. Defaulting to small_project.csv.");
            filePath = "./small_project.csv";
        }

        // Read activities from the chosen file
        activities = CSVReader.readActivitiesFromCsv(filePath);

        // Build the graph
        buildPertCmpGraphAux();

        /*
         * Exports the graph to DOT format for visualization and generates an image
         * if uncommented. These operations are currently disabled.
         *
         * String dotFilePath = "./trash/pert_cpm_graph.dot";
         * GraphVizExporter.exportToDot(graph, dotFilePath);
         * System.out.println("\nDOT file created: " + dotFilePath);
         *
         * String outputImagePath = "./trash/pert_cpm_graph.svg";
         * try {
         *     generateGraphImage(dotFilePath, outputImagePath);
         *     System.out.println("SVG image generated: " + outputImagePath);
         * } catch (IOException | InterruptedException e) {
         *     e.printStackTrace();
         *     System.err.println("Error generating the graph image.");
         * }
         */
    }

    /**
     * Method to build the PERT-CPM graph based on the activities duration changes.
     */
    private static void buildPertCpmGraph2() {
        buildPertCmpGraphAux();
    }

    /**
     * Auxiliary method to build the PERT-CPM graph.
     * This method was created in order to avoid code repetition.
     */
    private static void buildPertCmpGraphAux() {
        pertcpmGraph.buildGraph(activities);

        Graph<Activity, Integer> graph = pertcpmGraph.getGraph();

        System.out.println("\nGraph built successfully:");
        System.out.println("Number of Nodes: " + graph.numVertices());
        System.out.println("Number of edges: " + graph.numEdges());
        System.out.println("\nNodes:");
        for (Activity activity : graph.vertices()) {
            System.out.println(activity);
        }

        System.out.println("\nEdges:");
        for (Edge<Activity, Integer> edge : graph.edges()) {
            System.out.printf("%s -> %s (Duration: %d %s)\n",
                    edge.getVOrig(),
                    edge.getVDest(),
                    edge.getWeight(),
                    edge.getVOrig().getDurationUnit());
        }
    }

    /**
     * Validates the PERT-CPM graph for circular dependencies.
     * Throws an exception if cycles are detected in the graph.
     */
    private static void validateNoCircularDependencies() {
        try {
            pertcpmGraph.validateNoCircularDependencies();
            System.out.println("No circular dependencies detected. The project graph is valid.");
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Performs a topological sort of the activities in the graph
     * and displays the resulting order.
     */
    private static void performTopologicalSort() {
        String topologicalOrder = pertcpmGraph.getTopologicalSortAsString();
        System.out.println("\nTopological Sort Result:");
        System.out.println(topologicalOrder);
    }

    /**
     * Calculates the earliest and latest start and finish times
     * for each activity in the PERT-CPM graph and prints the results.
     */
    private static void calculateEarliestAndLatestTimes() {
        System.out.println("\nEarliest and Latest Start and Finish Times:");
        pertcpmGraph.calculateEarliestAndLatestTimes();
        pertcpmGraph.printActivityTimes();
    }

    /**
     * Exports the current project schedule to a CSV file.
     */
    private static void exportScheduleToCsv() {
        String scheduleFilePath = "./schedule.csv";
        pertcpmGraph.exportScheduleToCsv(scheduleFilePath);
    }

    /**
     * Identifies and marks the critical path in the PERT-CPM graph.
     */
    private static void identifyCriticalPath() {
        pertcpmGraph.identifyCriticalPath();
    }

    /**
     * Identifies bottleneck activities in the project, which are activities
     * that limit the overall project timeline, and displays their details.
     */
    private static void identifyBottleneckActivities() {
        List<Activity> bottleneckActivities = pertcpmGraph.identifyBottleneckActivities();
        System.out.println("\nBottleneck Activities: ");
        for (Activity activity : bottleneckActivities) {
            System.out.printf(
                    "ID: %s, Name: %s, Duration: %d days, ES: %d, EF: %d, LS: %d, LF: %d\n",
                    activity.getId(),
                    activity.getDescription(),
                    activity.getDuration(),
                    activity.getEarliestStart(),
                    activity.getEarliestFinish(),
                    activity.getLatestStart(),
                    activity.getLatestFinish()
            );
        }
    }

    /**
     * Generates a graph image from a DOT file using GraphViz.
     * @param dotFilePath Path to the DOT file.
     * @param outputImagePath Path to save the generated image.
     * @throws IOException If an error occurs during file operations.
     * @throws InterruptedException If the GraphViz process is interrupted.
     */
    public static void generateGraphImage(String dotFilePath, String outputImagePath) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tsvg", dotFilePath, "-o", outputImagePath);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();
    }

    /**
     * Displays the generated graph image using a Swing GUI.
     * @param imagePath Path to the image file to display.
     */
    private static void displayImage(String imagePath) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PERT/CPM Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JLabel label = new JLabel(new ImageIcon(imagePath));
            frame.getContentPane().add(label);

            frame.setVisible(true);
        });
    }


    /**
     * Displays a menu for simulating project delays and allows
     * users to perform various actions, such as modifying activity durations,
     * recalculating the graph, and identifying critical elements.
     */
    private static void simulateProjectDelaysMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean backToMenu = false;

        while (!backToMenu) {
            System.out.println("\n" + "═".repeat(40));
            System.out.println("⌛  \u001B[1mSIMULATE PROJECT DELAYS\u001B[0m");
            System.out.println("═".repeat(40) + "\n");

            System.out.println("🔹 1. Change Activity Duration ⏳");
            System.out.println("🔹 2. Build PERT-CPM Graph 📐");
            System.out.println("🔹 3. Perform Topological Sort 🗺️");
            System.out.println("🔹 4. Calculate Earliest and Latest Times ⏱️");
            System.out.println("🔹 5. Export Schedule to CSV 📤");
            System.out.println("🔹 6. Identify Critical Path 📍");
            System.out.println("🔹 7. Identify Bottleneck Activities 📌");
            System.out.println("\n\u001B[31m0️⃣  Back to Main Menu\u001B[0m 🚪");


            System.out.print("\nChoose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    changeActivityDuration();
                    break;
                case 2:
                    buildPertCpmGraph2();
                    break;
                case 3:
                    performTopologicalSort();
                    break;
                case 4:
                    calculateEarliestAndLatestTimes();
                    break;
                case 5:
                    exportScheduleToCsv();
                    break;
                case 6:
                    identifyCriticalPath();
                    break;
                case 7:
                    identifyBottleneckActivities();
                    break;
                case 0:
                    backToMenu = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Modifies the duration of a specific activity and updates the project graph.
     * Prompts the user to enter the activity ID and the new duration.
     */
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

        System.out.print("Enter the new duration for the activity: ");
        int newDuration = scanner.nextInt();

        activity.setDuration(newDuration);
        System.out.println("Activity duration updated.");

        // Recalculate the graph
        //pertcpmGraph.buildGraph(activities);
        pertcpmGraph.calculateEarliestAndLatestTimes();
    }

}
