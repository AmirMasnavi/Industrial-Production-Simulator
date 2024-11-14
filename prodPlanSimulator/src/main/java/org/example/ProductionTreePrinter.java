package org.example;

import java.util.List;
import java.util.Map;

public class ProductionTreePrinter {

    private final Map<Integer, List<int[]>> booData;

    public ProductionTreePrinter(Map<Integer, List<int[]>> booData) {
        this.booData = booData;
    }

    public void printTree(ProductionTreeNode root) {
        System.out.println("Production Tree for Item ID " + root.getItem().getId() + ":");
        printNode(root, 0, true);
    }

    private void printNode(ProductionTreeNode node, int level, boolean isLast) {
        String indentation = "  ".repeat(level);
        String branchSymbol = isLast ? "└── " : "├── ";

        // Format the node based on whether it's an item or an operation
        String nodeRepresentation = formatNode(node);
        System.out.println(indentation + branchSymbol + nodeRepresentation);

        // Recursively print the children, adjusting the level and branch symbols
        List<ProductionTreeNode> children = node.getChildren();
        int childCount = children.size();
        for (int i = 0; i < childCount; i++) {
            boolean isChildLast = (i == childCount - 1);
            printNode(children.get(i), level + 1, isChildLast);
        }
    }

    private String formatNode(ProductionTreeNode node) {
        StringBuilder nodeRepresentation = new StringBuilder();

        // Handle item nodes
        if (node.getItem() != null) {
            nodeRepresentation.append("<Item> ").append(node.getItem().getName());
        }

        // Handle operation nodes
        else if (node.getOperation() != null) {
            nodeRepresentation.append("[Operation] ").append(node.getOperation().getName());
        }

        // Display quantity if available
        if (node.getQuantity() != 0) {
            nodeRepresentation.append(" (Quantity: ").append(node.getQuantity()).append(")");
        }

        return nodeRepresentation.toString();
    }
}
