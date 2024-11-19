package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductionTreeBuilderOpID {

    private final List<Item> items;
    private final List<Operation> operations;
    private final Map<Integer, Map<Integer, Double>> booData;
    private final Map<Integer, Double> itemQuantities;

    // Constructor to initialize the builder with data
    public ProductionTreeBuilderOpID(List<Item> items, List<Operation> operations, Map<Integer, Map<Integer, Double>> booData, Map<Integer, Double> itemQuantities) {
        this.items = items;
        this.operations = operations;
        this.booData = booData;
        this.itemQuantities = itemQuantities;
    }

    public ProductionTreeNode buildTree(int operationId, Map<Integer, Integer> operationToItemMap) {
        Operation rootOperation = findOperationById(operationId);
        ProductionTreeNode rootNode = new ProductionTreeNode(rootOperation);

        Set<Integer> visitedOperations = new HashSet<>();
        buildSubTree(rootNode, visitedOperations, operationToItemMap);

        return rootNode;
    }

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

    // Helper method to find an item by its ID
    private Item findItemById(int id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found for ID: " + id));
    }

    // Helper method to find an operation by its ID
    private Operation findOperationById(int operationId) {
        return operations.stream()
                .filter(op -> op.getId() == operationId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Operation not found for ID: " + operationId));
    }
}
