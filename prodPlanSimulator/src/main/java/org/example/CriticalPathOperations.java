package org.example;

import java.util.*;

/**
 * This class handles operations related to calculating and processing the critical path
 * in a production process. It builds operation trees based on given operations, calculates
 * the depth of these trees, and prints them in the order of critical path.
 */
public class CriticalPathOperations {

    /**
     * This method processes the critical path operations by building operation trees,
     * calculating their depths, and printing the operations in the order of their
     * critical path, from the most to the least critical.
     *
     * @param items The list of items involved in the operations.
     * @param operations The list of operations to be processed.
     * @param booDataResult The result data for the Bill of Operations (BOO), containing
     *                      the mapping of operations to items and item quantities.
     */
    public static void criticalPathOperations(List<Item> items, List<Operation> operations, BooDataResult booDataResult) {
        // Create a map to store the mapping between operation ID and item ID
        Map<Integer, Integer> operationToItemMap = new HashMap<>();

        // Creating the tree builder that will be used to build operation trees
        ProductionTreeBuilderOpID treeBuilder = new ProductionTreeBuilderOpID(items, operations, booDataResult.booData, booDataResult.itemQuantities);

        // Map to store operations and their respective tree depths
        Map<Operation, Integer> operationDepths = new HashMap<>();

        // Calculate the depth of each tree by iterating over each operation
        for (Operation operation : operations) {
            // Build the tree for each operation
            ProductionTreeNode rootNode = treeBuilder.buildTree(operation.getId(), operationToItemMap);
            // Calculate the depth of the current operation's tree
            int depth = calculateTreeDepth(rootNode);
            // Store the calculated depth for the operation
            operationDepths.put(operation, depth);
        }

        // Using PriorityQueue to store operations ordered by tree depth (most critical first)
        PriorityQueue<Operation> priorityQueue = new PriorityQueue<>(
                (op1, op2) -> Integer.compare(operationDepths.get(op2), operationDepths.get(op1))
        );

        // Add all operations to the priority queue
        priorityQueue.addAll(operations);

        // Process operations in the order of their criticality (based on tree depth)
        ProductionTreePrinter printer = new ProductionTreePrinter(booDataResult.booData);
        while (!priorityQueue.isEmpty()) {
            // Poll the operation with the highest priority (most critical)
            Operation operation = priorityQueue.poll();
            // Print the critical path operation details
            System.out.println("\nCritical Path Operation for: " + operation.getName() + " (ID: " + operation.getId() + ")");
            // Build the operation tree for this operation
            ProductionTreeNode rootNode = treeBuilder.buildTree(operation.getId(), operationToItemMap);
            // Print the operation tree
            printer.printOperationTree(rootNode);
        }
    }

    /**
     * Recursively calculates the depth of a production tree. The depth is defined as the longest
     * path from the root node to any leaf node.
     *
     * @param node The root node of the tree whose depth is to be calculated.
     * @return The depth of the tree.
     */
    public static int calculateTreeDepth(ProductionTreeNode node) {
        // Base case: if the node is null or has no children, the depth is 1
        if (node == null || node.getChildren().isEmpty()) {
            return 1;
        }

        // Recursively calculate the depth of each child and return the maximum depth plus 1
        int maxDepth = 0;
        for (ProductionTreeNode child : node.getChildren()) {
            maxDepth = Math.max(maxDepth, calculateTreeDepth(child));
        }
        return maxDepth + 1;
    }
}
