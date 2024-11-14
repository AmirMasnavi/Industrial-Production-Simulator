package org.example;

import java.util.List;
import java.util.Map;

public class ProductionTreePrinter {

    private Map<Integer, List<int[]>> booData;

    // Constructor to initialize with booData
    public ProductionTreePrinter(Map<Integer, List<int[]>> booData) {
        this.booData = booData;
    }

    // Method to print the production tree starting from the root node
    public void printTree(ProductionTreeNode rootNode) {
        printNode(rootNode, "", true);
    }

    // Helper method to print a single node with the correct indentation
    private void printNode(ProductionTreeNode node, String indent, boolean isLast) {
        // Print item
        if (node.getItem() != null) {
            System.out.println(indent + (isLast ? "└── " : "├── ") + "<Item> " + node.getItem().getName() + " (Quantity: " + node.getQuantity() + ")");
        }

        // Print operation
        if (node.getOperation() != null) {
            System.out.println(indent + (isLast ? "└── " : "├── ") + "[Operation] " + node.getOperation().getName());
        }

        // Recurse through children
        for (int i = 0; i < node.getChildren().size(); i++) {
            printNode(node.getChildren().get(i), indent + (isLast ? "    " : "│   "), i == node.getChildren().size() - 1);
        }
    }
}

