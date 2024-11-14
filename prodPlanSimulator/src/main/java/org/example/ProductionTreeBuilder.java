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

    // Method to build the production tree for a specific item ID
    public ProductionTreeNode buildTree(int itemId) {
        // Get the item and its operation
        Item rootItem = findItemById(itemId);
        ProductionTreeNode rootNode = new ProductionTreeNode(rootItem);

        // Use a Set to track visited items and avoid infinite recursion
        Set<Integer> visitedItems = new HashSet<>();
        buildSubTree(rootNode, visitedItems);

        return rootNode;
    }

    // Method to build the subtree for a given node (recursively)
    private void buildSubTree(ProductionTreeNode node, Set<Integer> visitedItems) {
        Item item = node.getItem();

        // If this item has been visited, return immediately to prevent infinite recursion
        if (visitedItems.contains(item.getId())) {
            return;
        }

        // Mark the current item as visited
        visitedItems.add(item.getId());

        // Check if this item has any subcomponents or operations
        if (booData.containsKey(item.getId())) {
            List<int[]> subcomponents = booData.get(item.getId());

            for (int[] subcomponent : subcomponents) {
                int subItemId = subcomponent[0];
                double quantity = subcomponent[1] / 1000.0;

                Item subItem = findItemById(subItemId);
                Operation operation = findOperationByItemId(item.getId());

                // Create a new production tree node for the subcomponent
                ProductionTreeNode subNode = new ProductionTreeNode(subItem);
                subNode.setQuantity(quantity);

                // Add the operation if needed
                if (operation != null) {
                    ProductionTreeNode operationNode = new ProductionTreeNode(operation);
                    subNode.addChild(operationNode);
                }

                // Add subcomponent node to the current node
                node.addChild(subNode);

                // Recurse to build the subtree for the subcomponent
                buildSubTree(subNode, visitedItems);
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
    private Operation findOperationByItemId(int itemId) {
        // Find the corresponding operation for the item from the operations list
        return operations.stream()
                .filter(operation -> booData.containsKey(itemId))
                .findFirst()
                .orElse(null);  // Or handle a case where no operation is found
    }
}
