package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builds a hierarchical production tree from a list of items, operations, and their relationships.
 * This tree represents the dependencies between items and operations as defined in the Bill of Operations (BOO) data.
 */
public class ProductionTreeBuilder {

    // Fields to hold the data required for building the tree
    private final List<Item> items;                           // List of all items
    private final List<Operation> operations;                 // List of all operations
    private final Map<Integer, Map<Integer, Double>> booData; // BOO data mapping item IDs to their subcomponents
    private final Map<Integer, Double> itemQuantities;        // Map of item IDs to their quantities

    /**
     * Constructor to initialize the ProductionTreeBuilder with necessary data.
     *
     * @param items          list of all items
     * @param operations     list of all operations
     * @param booData        map defining BOO relationships (item ID -> subcomponents and their quantities)
     * @param itemQuantities map of item IDs to their quantities
     */
    public ProductionTreeBuilder(List<Item> items, List<Operation> operations, Map<Integer, Map<Integer, Double>> booData, Map<Integer, Double> itemQuantities) {
        this.items = items;
        this.operations = operations;
        this.booData = booData;
        this.itemQuantities = itemQuantities;
    }

    /**
     * Builds the root of a production tree for a given item and recursively constructs its sub-tree.
     *
     * @param itemId             the ID of the root item
     * @param operationToItemMap a map linking operation IDs to their associated items
     * @return the root of the production tree
     */
    public ProductionTreeNode buildTree(int itemId, Map<Integer, Integer> operationToItemMap) {
        Item rootItem = findItemById(itemId);
        ProductionTreeNode rootNode = new ProductionTreeNode(rootItem);

        // Set the quantity of the root node using the provided quantities map
        if (itemQuantities.containsKey(itemId)) {
            rootNode.setQuantity(itemQuantities.get(itemId));
        }

        // Sets to track visited items and added operations to prevent redundancy
        Set<Integer> visitedItems = new HashSet<>();
        Set<Integer> addedOperations = new HashSet<>();
        buildSubTree(rootNode, visitedItems, addedOperations, operationToItemMap);

        return rootNode;
    }

    /**
     * Recursively builds the sub-tree for a given node, linking operations and subcomponents.
     *
     * @param node               the current node being processed
     * @param visitedItems       a set of already visited item IDs to avoid infinite loops
     * @param addedOperations    a set of already added operation IDs to prevent duplication
     * @param operationToItemMap a map linking operation IDs to their associated items
     */
    private void buildSubTree(ProductionTreeNode node, Set<Integer> visitedItems, Set<Integer> addedOperations, Map<Integer, Integer> operationToItemMap) {
        Item item = node.getItem();

        // Avoid processing an item multiple times
        if (visitedItems.contains(item.getId())) {
            return;
        }

        visitedItems.add(item.getId());

        // Check if this item has any subcomponents in the BOO data
        if (booData.containsKey(item.getId())) {
            Map<Integer, Double> subcomponents = booData.get(item.getId());

            // Add the operation node if applicable
            Operation operation = findOperationByItemId(item.getId(), operationToItemMap);
            if (operation != null && !addedOperations.contains(operation.getId())) {
                ProductionTreeNode operationNode = new ProductionTreeNode(operation);
                node.addChild(operationNode); // Attach the operation node to the item node
                addedOperations.add(operation.getId());
            }

            // Process subcomponents of the item
            for (Map.Entry<Integer, Double> subcomponent : subcomponents.entrySet()) {
                int subId = subcomponent.getKey();
                double quantity = subcomponent.getValue();

                if (operationToItemMap.containsKey(subId)) {
                    // Subcomponent is an operation; resolve its associated item
                    int resolvedItemId = operationToItemMap.get(subId);
                    Operation subOperation = findOperationById(subId);
                    Item resolvedItem = findItemById(resolvedItemId);

                    // Add the operation node and its resolved item sub-node
                    if (!addedOperations.contains(subOperation.getId())) {
                        ProductionTreeNode operationNode = new ProductionTreeNode(subOperation);
                        node.addChild(operationNode);
                        addedOperations.add(subOperation.getId());

                        ProductionTreeNode resolvedItemNode = new ProductionTreeNode(resolvedItem);
                        resolvedItemNode.setQuantity(quantity);
                        operationNode.addChild(resolvedItemNode);

                        // Recursively build the tree for the resolved item
                        buildSubTree(resolvedItemNode, visitedItems, addedOperations, operationToItemMap);
                    }
                } else {
                    // Subcomponent is a standard item
                    Item subItem = findItemById(subId);
                    ProductionTreeNode subNode = new ProductionTreeNode(subItem);
                    subNode.setQuantity(quantity);

                    node.addChild(subNode);

                    // Recursively build the tree for the sub-item
                    buildSubTree(subNode, visitedItems, addedOperations, operationToItemMap);
                }
            }
        }
    }

    /**
     * Finds an item by its ID in the list of items.
     *
     * @param id the ID of the item to find
     * @return the matching item
     * @throws IllegalArgumentException if no item with the specified ID is found
     */
    private Item findItemById(int id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found for ID: " + id));
    }

    /**
     * Finds an operation associated with a given item ID based on the operation-to-item map.
     *
     * @param itemId             the ID of the item
     * @param operationToItemMap a map linking operation IDs to item IDs
     * @return the matching operation, or null if no operation is associated with the item
     */
    private Operation findOperationByItemId(int itemId, Map<Integer, Integer> operationToItemMap) {
        return operations.stream()
                .filter(operation -> operationToItemMap.get(operation.getId()) != null
                        && operationToItemMap.get(operation.getId()) == itemId)
                .findFirst()
                .orElse(null); // Return null if no operation is found
    }

    /**
     * Finds an operation by its ID in the list of operations.
     *
     * @param operationId the ID of the operation to find
     * @return the matching operation
     * @throws IllegalArgumentException if no operation with the specified ID is found
     */
    private Operation findOperationById(int operationId) {
        return operations.stream()
                .filter(op -> op.getId() == operationId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Operation not found for ID: " + operationId));
    }
}
