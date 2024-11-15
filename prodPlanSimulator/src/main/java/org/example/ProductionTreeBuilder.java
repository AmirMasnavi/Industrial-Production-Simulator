package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductionTreeBuilder {

    private List<Item> items;
    private List<Operation> operations;
    private Map<Integer, List<int[]>> booData;

    // Constructor to initialize the builder with data
    public ProductionTreeBuilder(List<Item> items, List<Operation> operations, Map<Integer, List<int[]>> booData) {
        this.items = items;
        this.operations = operations;
        this.booData = booData;
    }

    public ProductionTreeNode buildTree(int itemId, Map<Integer, Integer> operationToItemMap) {
        Item rootItem = findItemById(itemId);
        ProductionTreeNode rootNode = new ProductionTreeNode(rootItem);

        Set<Integer> visitedItems = new HashSet<>();
        buildSubTree(rootNode, visitedItems, operationToItemMap);

        return rootNode;
    }

    // Method to build the subtree for a given node (recursively)
    private void buildSubTree(ProductionTreeNode node, Set<Integer> visitedItems, Map<Integer, Integer> operationToItemMap) {
        Item item = node.getItem();

        if (visitedItems.contains(item.getId())) {
            return;
        }

        visitedItems.add(item.getId());

        if (booData.containsKey(item.getId())) {
            List<int[]> subcomponents = booData.get(item.getId());

            // Find and add the operation directly under the item node
            Operation operation = findOperationByItemId(item.getId(), operationToItemMap);
            if (operation != null) {
                ProductionTreeNode operationNode = new ProductionTreeNode(operation);
                node.addChild(operationNode); // Add operation as a direct child of the item
            }

            // Add subcomponents recursively
            for (int[] subcomponent : subcomponents) {
                int subItemId = subcomponent[0];
                double quantity = subcomponent[1] / 1000.0;

                Item subItem = findItemById(subItemId);

                ProductionTreeNode subNode = new ProductionTreeNode(subItem);
                subNode.setQuantity(quantity);

                node.addChild(subNode);
                buildSubTree(subNode, visitedItems, operationToItemMap);
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
}
