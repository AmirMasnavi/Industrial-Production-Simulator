package org.example;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for searching and indexing a production tree structure by name or ID.
 * Provides methods to search nodes, retrieve details, and calculate the depth of nodes in the tree.
 */
public class ProductionTreeSearcher {
    private final Map<String, ProductionTreeNode> nameMap; // Map for searching by name
    private final Map<String, ProductionTreeNode> idMap;   // Map for searching by ID

    /**
     * Constructor to initialize the searcher with empty search maps.
     */
    public ProductionTreeSearcher() {
        this.nameMap = new HashMap<>();
        this.idMap = new HashMap<>();
    }

    /**
     * Indexes the entire production tree for efficient searching by name and ID.
     *
     * @param rootNode the root node of the production tree.
     */
    public void indexTree(ProductionTreeNode rootNode) {
        indexNode(rootNode, null); // Begin indexing with no parent operation
    }

    /**
     * Recursively indexes a node and its children, storing them in search maps.
     *
     * @param node            the current node being indexed.
     * @param parentOperation the parent operation of the current node, or null if none exists.
     */
    private void indexNode(ProductionTreeNode node, Operation parentOperation) {
        // Index by name and ID, based on whether the node represents an item or operation
        if (node.getItem() != null) {
            nameMap.put(node.getItem().getName(), node);
            idMap.put(String.valueOf(node.getItem().getId()), node);
        } else if (node.getOperation() != null) {
            nameMap.put(node.getOperation().getName(), node);
            idMap.put(String.valueOf(node.getOperation().getId()), node);
        }

        // Update the parent operation reference for the current node
        node.setParentOperation(parentOperation);

        // Recursively index child nodes
        for (ProductionTreeNode child : node.getChildren()) {
            indexNode(child, node.getOperation() != null ? node.getOperation() : parentOperation);
        }
    }

    /**
     * Searches for a node by its name or ID and returns its formatted details.
     *
     * @param searchTerm the name or ID of the node to search for.
     * @return a formatted string containing the node's details, or a message if the node is not found.
     */
    public String search(String searchTerm) {
        // Attempt to search by ID
        ProductionTreeNode nodeById = idMap.get(searchTerm);
        if (nodeById != null) {
            String title = "=== Search Result for ID: " + searchTerm + " ===\n";
            return title + getNodeDetails(nodeById);
        }

        // Attempt to search by name
        ProductionTreeNode nodeByName = nameMap.get(searchTerm);
        if (nodeByName != null) {
            String title = "=== Search Result for Name: " + searchTerm + " ===\n";
            return title + getNodeDetails(nodeByName);
        }

        // Return a not-found message if no matches are found
        return "=== No results found for: " + searchTerm + " ===";
    }

    /**
     * Retrieves a node by its name or ID.
     *
     * @param searchTerm the name or ID of the node to retrieve.
     * @return the corresponding node, or null if not found.
     */
    public ProductionTreeNode getNodeByNameOrId(String searchTerm) {
        // Search by name first
        ProductionTreeNode nodeByName = nameMap.get(searchTerm);
        if (nodeByName != null) {
            return nodeByName;
        }

        // Fallback to search by ID
        try {
            return idMap.get(searchTerm);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Calculates the depth of a node within the production tree.
     *
     * @param searchTerm the name or ID of the node whose depth is to be calculated.
     * @return the depth of the node, or -1 if the node is not found.
     */
    public int calculateNodeDepth(String searchTerm) {
        ProductionTreeNode node = getNodeByNameOrId(searchTerm);

        if (node == null) {
            System.out.println("Node not found: " + searchTerm);
            return -1;
        }

        int depth = 0;

        // Traverse up the tree to calculate depth
        while (node.getParentOperation() != null) {
            node = getNodeByOperation(node.getParentOperation());
            if (node == null) {
                break;
            }
            depth++;
        }

        return depth;
    }

    /**
     * Searches for a node corresponding to a given operation.
     *
     * @param operation the operation to search for.
     * @return the node associated with the operation, or null if not found.
     */
    private ProductionTreeNode getNodeByOperation(Operation operation) {
        for (ProductionTreeNode node : idMap.values()) {
            if (node.getOperation() != null && node.getOperation().equals(operation)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Formats and retrieves details of a node for display purposes.
     *
     * @param node the node whose details are to be retrieved.
     * @return a formatted string containing the node's details.
     */
    private String getNodeDetails(ProductionTreeNode node) {
        StringBuilder details = new StringBuilder();

        // Include item details if the node represents an item
        if (node.getItem() != null) {
            details.append("Type: Material\n");
            details.append("Name: ").append(node.getItem().getName()).append("\n");
            details.append("ID: ").append(node.getItem().getId()).append("\n");
            details.append("Quantity: ").append(node.getQuantity()).append("\n");
        }
        // Include operation details if the node represents an operation
        else if (node.getOperation() != null) {
            details.append("Type: Operation\n");
            details.append("Name: ").append(node.getOperation().getName()).append("\n");
            details.append("ID: ").append(node.getOperation().getId()).append("\n");
        }

        // Add parent operation details if available
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
