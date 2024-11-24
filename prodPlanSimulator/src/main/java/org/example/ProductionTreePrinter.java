package org.example;

import java.util.List;
import java.util.Map;

/**
 * A utility class for printing a production tree in a structured and readable format.
 * The tree can represent a hierarchy of operations and items, with optional quantities.
 */
public class ProductionTreePrinter {
    private final Map<Integer, Map<Integer, Double>> booData; // Data mapping items to their subcomponents

    /**
     * Constructor to initialize the printer with Bill of Operations (BOO) data.
     *
     * @param booData A map representing subcomponents for each item. Keys are item IDs,
     *                and values are maps of subcomponent IDs to quantities.
     */
    public ProductionTreePrinter(Map<Integer, Map<Integer, Double>> booData) {
        this.booData = booData;
    }

    /**
     * Prints the entire production tree starting from the root node, with item names and quantities.
     *
     * @param rootNode The root node of the production tree to print.
     */
    public void printTree(ProductionTreeNode rootNode) {
        System.out.println("Production Tree:");
        printNode(rootNode, "", true);
    }

    /**
     * Recursively prints each node in the production tree with appropriate formatting for hierarchy.
     * Each node's type, name, and quantity (if applicable) are displayed.
     *
     * @param node    The current node to print.
     * @param prefix  The prefix string to format the tree structure.
     * @param isLast  A flag indicating whether the current node is the last sibling.
     */
    private void printNode(ProductionTreeNode node, String prefix, boolean isLast) {
        // Print operation details if the node represents an operation
        if (node.getOperation() != null) {
            System.out.print(prefix);
            System.out.print(isLast ? "└── " : "├── ");
            System.out.println("[Op" + node.getOperation().getId() + "]: " + node.getOperation().getName());
        }

        // Print item details, including quantity if available
        if (node.getItem() != null) {
            System.out.print(prefix);
            System.out.print(isLast ? "└── " : "├── ");
            System.out.print(node.getItem().getName() + " (Item " + node.getItem().getId() + ")");
            if (node.getQuantity() > 0) {
                System.out.print(" - Quantity: " + node.getQuantity());
            }
            System.out.println();
        }

        // Handle subcomponents for items without children or operations
        if (node.getChildren().isEmpty() && node.getItem() != null && booData.get(node.getItem().getId()) == null) {
            System.out.print(prefix);
            System.out.println(isLast ? "    └─ Material: " + node.getItem().getName() : "    ├─ Material: " + node.getItem().getName());
        } else {
            // Recursively print child nodes
            List<ProductionTreeNode> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                printNode(children.get(i), prefix + (isLast ? "    " : "│   "), i == children.size() - 1);
            }
        }
    }

    /**
     * Prints the tree structure focusing solely on operations, starting from the root node.
     *
     * @param rootNode The root node of the operation tree to print.
     */
    public void printOperationTree(ProductionTreeNode rootNode) {
        System.out.println("Operation Tree:");
        printOperationNode(rootNode, "", true);
    }

    /**
     * Recursively prints each operation in the production tree with appropriate formatting.
     *
     * @param node    The current node to print.
     * @param prefix  The prefix string to format the tree structure.
     * @param isLast  A flag indicating whether the current node is the last sibling.
     */
    private void printOperationNode(ProductionTreeNode node, String prefix, boolean isLast) {
        // Print operation details
        if (node.getOperation() != null) {
            System.out.print(prefix);
            System.out.print(isLast ? "└── " : "├── ");
            System.out.println("[Op" + node.getOperation().getId() + "]: " + node.getOperation().getName());
        }

        // Recursively print child nodes that represent operations
        List<ProductionTreeNode> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            printOperationNode(children.get(i), prefix + (isLast ? "    " : "│   "), i == children.size() - 1);
        }
    }
}
