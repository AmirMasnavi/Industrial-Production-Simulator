package org.example;

import java.util.*;

public class ProductionTreeBuilder {
    private final List<Item> items;
    private final List<Operation> operations;
    private final Map<Integer, List<int[]>> booData;

    // A set to keep track of visited nodes to prevent cycles (to avoid infinite recursion)
    private Set<Integer> visitedItems;
    private Set<Integer> visitedOperations;

    public ProductionTreeBuilder(List<Item> items, List<Operation> operations, Map<Integer, List<int[]>> booData) {
        this.items = items;
        this.operations = operations;
        this.booData = booData;
    }

    // Builds the production tree for a specific item ID
    public ProductionTreeNode buildTree(int rootItemId) {
        Item rootItem = findItemById(rootItemId);
        if (rootItem == null) {
            throw new IllegalArgumentException("Item with ID " + rootItemId + " not found.");
        }

        // Create the root node
        ProductionTreeNode rootNode = new ProductionTreeNode(rootItem);

        // Initialize visited sets
        visitedItems = new HashSet<>();
        visitedOperations = new HashSet<>();

        // Recursively build the tree
        buildProductionTree(rootNode);

        return rootNode;
    }

    private void buildProductionTree(ProductionTreeNode node) {
        // Handle operations first, if they are part of this node
        if (node.getItem() != null) {
            Integer itemId = node.getItem().getId();

            if (visitedItems.contains(itemId)) {
                return;  // Skip if the item has already been processed
            }

            visitedItems.add(itemId);  // Mark the item as visited

            // Check if the item has subcomponents (either operations or items)
            if (booData.containsKey(itemId)) {
                List<int[]> subcomponents = booData.get(itemId);
                for (int[] component : subcomponents) {
                    int componentId = component[0];
                    double quantity = component[1] / 1000.0;  // Convert to decimal (if necessary)

                    // Check if the component is an operation
                    Operation operation = findOperationById(componentId);
                    if (operation != null) {
                        // First, add the operation node
                        ProductionTreeNode operationNode = new ProductionTreeNode(operation);
                        node.addChild(operationNode);

                        // Recursively build the tree for the operation
                        buildProductionTree(operationNode);
                    } else {
                        // If it's an item, add the item node
                        Item subItem = findItemById(componentId);
                        if (subItem != null) {
                            ProductionTreeNode subItemNode = new ProductionTreeNode(subItem);
                            subItemNode.setQuantity(quantity);
                            node.addChild(subItemNode);

                            // Recursively build the tree for the sub-item
                            buildProductionTree(subItemNode);
                        } else {
                            System.err.println("Item or operation with ID " + componentId + " not found.");
                        }
                    }
                }
            }
        }

        // Handle operations: check if this node is an operation
        if (node.getOperation() != null) {
            Integer operationId = node.getOperation().getId();

            if (visitedOperations.contains(operationId)) {
                return;  // Skip if the operation has already been processed
            }

            visitedOperations.add(operationId);  // Mark the operation as visited

            // Operations don't have children, but we may want to handle them here if necessary
        }
    }





    // Helper method to find an item by its ID
    private Item findItemById(int itemId) {
        for (Item item : items) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        return null;
    }

    // Helper method to find an operation by its ID
    private Operation findOperationById(int operationId) {
        for (Operation operation : operations) {
            if (operation.getId() == operationId) {
                return operation;
            }
        }
        return null;
    }
}
