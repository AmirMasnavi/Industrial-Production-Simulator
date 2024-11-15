package org.example;

import java.util.List;
import java.util.Map;

public class ProductionTreePrinter {
    private final Map<Integer, List<int[]>> booData;

    // Constructor
    public ProductionTreePrinter(Map<Integer, List<int[]>> booData) {
        this.booData = booData;
    }

    // Entry point to print the tree with title and quantity
    public void printTree(ProductionTreeNode rootNode) {
        System.out.println("Production Tree:");
        printNode(rootNode, "", true);
    }

    // Recursive method to print each node with quantity and formatting
    private void printNode(ProductionTreeNode node, String prefix, boolean isLast) {
        // Format for Operation first, before Item
        if (node.getOperation() != null) {
            System.out.print(prefix);
            System.out.print(isLast ? "└── " : "├── ");
            System.out.println("[Op" + node.getOperation().getId() + "]: " + node.getOperation().getName());
        }

        // Format for Item with quantity
        if (node.getItem() != null) {
            System.out.print(prefix);
            System.out.print(isLast ? "└── " : "├── ");
            System.out.print(node.getItem().getName() + " (Item " + node.getItem().getId() + ")");
            if (node.getQuantity() > 0) {
                System.out.print(" - Quantity: " + node.getQuantity());
            }
            System.out.println();
        }

        // Handle subcomponents or material
        if (node.getChildren().isEmpty() && node.getItem() != null && booData.get(node.getItem().getId()) == null) {
            System.out.print(prefix);
            System.out.println(isLast ? "    └─ Material: " + node.getItem().getName() : "    ├─ Material: " + node.getItem().getName());
        } else {
            // Recurse for children
            List<ProductionTreeNode> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                printNode(children.get(i), prefix + (isLast ? "    " : "│   "), i == children.size() - 1);
            }
        }
    }
}
