package org.example;

import java.util.HashMap;
import java.util.Map;

public class ProductionTreeSearcher {
    private Map<String, ProductionTreeNode> nameMap; // Map to search by name
    private Map<String, ProductionTreeNode> idMap;   // Map to search by ID

    // Constructor
    public ProductionTreeSearcher() {
        this.nameMap = new HashMap<>();
        this.idMap = new HashMap<>();
    }

    /**
     * Index the production tree for efficient searching.
     * @param rootNode the root node of the production tree.
     */
    public void indexTree(ProductionTreeNode rootNode) {
        indexNode(rootNode, null); // Start indexing from the root node
    }

    /**
     * Recursively index a node and its children.
     * @param node the current node being indexed.
     * @param parentOperation the parent operation, if any.
     */
    private void indexNode(ProductionTreeNode node, Operation parentOperation) {
        // Index the current node by name and ID
        if (node.getItem() != null) {
            nameMap.put(node.getItem().getName(), node);
            idMap.put(String.valueOf(node.getItem().getId()), node);
        } else if (node.getOperation() != null) {
            nameMap.put(node.getOperation().getName(), node);
            idMap.put("" + node.getOperation().getId(), node);
        }

        // Update the node's parent operation reference
        node.setParentOperation(parentOperation);

        // Recurse for children
        for (ProductionTreeNode child : node.getChildren()) {
            indexNode(child, node.getOperation() != null ? node.getOperation() : parentOperation);
        }
    }

    /**
     * Search for a node by name or ID and return formatted details.
     * @param searchTerm the name or ID to search for.
     * @return the formatted details of the found node, or a message if not found.
     */
    public String search(String searchTerm) {
        // Search by ID first
        ProductionTreeNode nodeById = idMap.get(searchTerm);
        if (nodeById != null) {
            String title = "=== Search Result for ID: " + searchTerm + " ===\n";
            return title + getNodeDetails(nodeById);
        }

        // Search by Name
        ProductionTreeNode nodeByName = nameMap.get(searchTerm);
        if (nodeByName != null) {
            String title = "=== Search Result for Name: " + searchTerm + " ===\n";
            return title + getNodeDetails(nodeByName);
        }

        // If not found
        return "=== No results found for: " + searchTerm + " ===";
    }

    /**
     * Get formatted details of a node.
     * @param node the node whose details are to be retrieved.
     * @return the formatted details.
     */
    private String getNodeDetails(ProductionTreeNode node) {
        StringBuilder details = new StringBuilder();

        if (node.getItem() != null) {
            details.append("Type: Material\n");
            details.append("Name: ").append(node.getItem().getName()).append("\n");
            details.append("ID: ").append(node.getItem().getId()).append("\n");
            details.append("Quantity: ").append(node.getQuantity()).append("\n");
        } else if (node.getOperation() != null) {
            details.append("Type: Operation\n");
            details.append("Name: ").append(node.getOperation().getName()).append("\n");
            details.append("ID: ").append(node.getOperation().getId()).append("\n");
        }

        if (node.getParentOperation() != null) {
            details.append("Parent Operation: [Op")
                    .append(node.getParentOperation().getId())
                    .append("] ")
                    .append(node.getParentOperation().getName())
                    .append("\n");
        }

        return details.toString();
    }
}
