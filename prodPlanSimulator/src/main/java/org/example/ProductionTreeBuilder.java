package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductionTreeBuilder {

    private List<Item> items;
    private List<Operation> operations;
    private Map<Integer, List<int[]>> booData;
    private Map<Integer, Integer> itemQuantities;

    // Constructor to initialize the builder with data
    public ProductionTreeBuilder(List<Item> items, List<Operation> operations, Map<Integer, List<int[]>> booData, Map<Integer, Integer> itemQuantities) {
        this.items = items;
        this.operations = operations;
        this.booData = booData;
        this.itemQuantities = itemQuantities;
    }

    public ProductionTreeNode buildTree(int itemId, Map<Integer, Integer> operationToItemMap) {
        Item rootItem = findItemById(itemId);
        ProductionTreeNode rootNode = new ProductionTreeNode(rootItem);

        // Set root node quantity using itemQuantities
        if (itemQuantities.containsKey(itemId)) {
            rootNode.setQuantity(itemQuantities.get(itemId) / 1000.0);
        }

        Set<Integer> visitedItems = new HashSet<>();
        buildSubTree(rootNode, visitedItems, operationToItemMap);

        return rootNode;
    }


    private void buildSubTree(ProductionTreeNode node, Set<Integer> visitedItems, Map<Integer, Integer> operationToItemMap) {
        Item item = node.getItem();

        // Prevent infinite recursion by skipping already visited items
        if (visitedItems.contains(item.getId())) {
            return;
        }

        visitedItems.add(item.getId());

        // Check if this item has any subcomponents
        if (booData.containsKey(item.getId())) {
            List<int[]> subcomponents = booData.get(item.getId());

            // Add operation node if it exists for this item
            Operation operation = findOperationByItemId(item.getId(), operationToItemMap);
            if (operation != null) {
                ProductionTreeNode operationNode = new ProductionTreeNode(operation);
                node.addChild(operationNode); // Add operation as a direct child of the item
            }

            // Process subcomponents
            for (int[] subcomponent : subcomponents) {
                int subId = subcomponent[0];
                double quantity = subcomponent[1] / 1000.0;

                if (operationToItemMap.containsKey(subId)) {
                    // Subcomponent is an operation; resolve its associated item
                    int resolvedItemId = operationToItemMap.get(subId);
                    Operation subOperation = findOperationById(subId);
                    Item resolvedItem = findItemById(resolvedItemId);

                    // Create and link operation node
                    ProductionTreeNode operationNode = new ProductionTreeNode(subOperation);
                    node.addChild(operationNode);

                    // Create and link resolved item node under the operation
                    ProductionTreeNode resolvedItemNode = new ProductionTreeNode(resolvedItem);
                    resolvedItemNode.setQuantity(quantity);
                    operationNode.addChild(resolvedItemNode);

                    // Recursively build the subtree for the resolved item
                    buildSubTree(resolvedItemNode, visitedItems, operationToItemMap);
                } else {
                    // Subcomponent is a regular item
                    Item subItem = findItemById(subId);
                    ProductionTreeNode subNode = new ProductionTreeNode(subItem);
                    subNode.setQuantity(quantity);

                    node.addChild(subNode);

                    // Recursively build the subtree for the sub-item
                    buildSubTree(subNode, visitedItems, operationToItemMap);
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

    // Helper method to find an operation by item ID
    private Operation findOperationByItemId(int itemId, Map<Integer, Integer> operationToItemMap) {
        return operations.stream()
                .filter(operation -> operationToItemMap.get(operation.getId()) != null
                        && operationToItemMap.get(operation.getId()) == itemId)
                .findFirst()
                .orElse(null); // Return null if no operation is found
    }

    // Helper method to find an operation by its ID
    private Operation findOperationById(int operationId) {
        return operations.stream()
                .filter(op -> op.getId() == operationId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Operation not found for ID: " + operationId));
    }

}
