package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class builds a hierarchical production tree starting from a root operation.
 * It utilizes relationships defined between operations and items in a Bill of Operations (BOO) structure.
 * The tree can represent both operations and associated items in a structured format.
 */
public class ProductionTreeBuilderOpID {

    private final List<Item> items; // List of all items available
    private final List<Operation> operations; // List of all operations available
    private final Map<Integer, Map<Integer, Double>> booData; // BOO data linking items to subcomponents
    private final Map<Integer, Double> itemQuantities; // Quantities for items in the production tree

    /**
     * Constructor to initialize the builder with the necessary data.
     *
     * @param items          List of items involved in production.
     * @param operations     List of operations involved in production.
     * @param booData        Mapping of item IDs to their subcomponents and quantities.
     * @param itemQuantities Mapping of item IDs to their quantities in production.
     */
    public ProductionTreeBuilderOpID(List<Item> items, List<Operation> operations, Map<Integer, Map<Integer, Double>> booData, Map<Integer, Double> itemQuantities) {
        this.items = items;
        this.operations = operations;
        this.booData = booData;
        this.itemQuantities = itemQuantities;
    }

    /**
     * Builds a production tree starting from a root operation.
     *
     * @param operationId         The ID of the root operation to start the tree.
     * @param operationToItemMap  A mapping of operation IDs to their associated item IDs.
     * @return A {@link ProductionTreeNode} representing the root of the production tree.
     */
    public ProductionTreeNode buildTree(int operationId, Map<Integer, Integer> operationToItemMap) {
        Operation rootOperation = findOperationById(operationId);
        ProductionTreeNode rootNode = new ProductionTreeNode(rootOperation);

        Set<Integer> visitedOperations = new HashSet<>();
        buildSubTree(rootNode, visitedOperations, operationToItemMap);

        return rootNode;
    }

    /**
     * Builds the subtree for a given operation node by recursively adding its associated items and subcomponents.
     *
     * @param node                The current operation node being processed.
     * @param visitedOperations   A set of already visited operation IDs to prevent infinite recursion.
     * @param operationToItemMap  A mapping of operation IDs to their associated item IDs.
     */
    private void buildSubTree(ProductionTreeNode node, Set<Integer> visitedOperations, Map<Integer, Integer> operationToItemMap) {
        Operation operation = node.getOperation();

        // Prevent infinite recursion by skipping already visited operations
        if (visitedOperations.contains(operation.getId())) {
            return;
        }

        visitedOperations.add(operation.getId());

        // Resolve the associated item for this operation, if it exists
        Integer associatedItemId = operationToItemMap.get(operation.getId());
        if (associatedItemId != null) {
            Item associatedItem = findItemById(associatedItemId);
            ProductionTreeNode itemNode = new ProductionTreeNode(associatedItem);

            // Set the quantity for the item if available
            if (itemQuantities.containsKey(associatedItemId)) {
                itemNode.setQuantity(itemQuantities.get(associatedItemId));
            }

            node.addChild(itemNode);

            // Recursively process the subtree for this item
            buildItemSubTree(itemNode, new HashSet<>(), operationToItemMap);
        }
    }

    /**
     * Builds the subtree for a given item node by recursively adding its subcomponents.
     *
     * @param node                The current item node being processed.
     * @param visitedItems        A set of already visited item IDs to prevent infinite recursion.
     * @param operationToItemMap  A mapping of operation IDs to their associated item IDs.
     */
    private void buildItemSubTree(ProductionTreeNode node, Set<Integer> visitedItems, Map<Integer, Integer> operationToItemMap) {
        Item item = node.getItem();

        // Prevent infinite recursion by skipping already visited items
        if (visitedItems.contains(item.getId())) {
            return;
        }

        visitedItems.add(item.getId());

        // Check if this item has subcomponents
        if (booData.containsKey(item.getId())) {
            Map<Integer, Double> subcomponents = booData.get(item.getId());

            // Process each subcomponent
            for (Map.Entry<Integer, Double> subcomponent : subcomponents.entrySet()) {
                int subId = subcomponent.getKey();
                double quantity = subcomponent.getValue();

                if (operationToItemMap.containsKey(subId)) {
                    // Subcomponent is an operation; resolve its associated item
                    Operation subOperation = findOperationById(subId);
                    int resolvedItemId = operationToItemMap.get(subId);
                    Item resolvedItem = findItemById(resolvedItemId);

                    // Create and link operation node
                    ProductionTreeNode operationNode = new ProductionTreeNode(subOperation);
                    node.addChild(operationNode);

                    // Create and link resolved item node under the operation
                    ProductionTreeNode resolvedItemNode = new ProductionTreeNode(resolvedItem);
                    resolvedItemNode.setQuantity(quantity);
                    operationNode.addChild(resolvedItemNode);

                    // Recursively build the subtree for the resolved item
                    buildItemSubTree(resolvedItemNode, visitedItems, operationToItemMap);
                } else {
                    // Subcomponent is a regular item
                    Item subItem = findItemById(subId);
                    ProductionTreeNode subNode = new ProductionTreeNode(subItem);
                    subNode.setQuantity(quantity);

                    node.addChild(subNode);

                    // Recursively build the subtree for the sub-item
                    buildItemSubTree(subNode, visitedItems, operationToItemMap);
                }
            }
        }
    }

    /**
     * Helper method to find an item by its ID.
     *
     * @param id The ID of the item to find.
     * @return The {@link Item} corresponding to the provided ID.
     * @throws IllegalArgumentException If the item is not found.
     */
    private Item findItemById(int id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found for ID: " + id));
    }

    /**
     * Helper method to find an operation by its ID.
     *
     * @param operationId The ID of the operation to find.
     * @return The {@link Operation} corresponding to the provided ID.
     * @throws IllegalArgumentException If the operation is not found.
     */
    private Operation findOperationById(int operationId) {
        return operations.stream()
                .filter(op -> op.getId() == operationId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Operation not found for ID: " + operationId));
    }
}
