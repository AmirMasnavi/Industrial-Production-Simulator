package org.example;

import java.util.Map;

/**
 * Represents a node in a balanced tree, using string-based identifiers.
 */
class BalancedNode {
    String operationId; // Identifier for the operation
    String partId; // Identifier for the associated part
    Map<String, Double> dependencies; // Map of dependencies with quantities
    BalancedNode leftChild, rightChild; // References to left and right child nodes
    int nodeHeight; // Height of the node in the tree
    private BalancedNode rootNode;

    /**
     * Constructs a new balanced node.
     *
     * @param operationId  Identifier for the operation
     * @param partId       Identifier for the associated part
     * @param dependencies Map of dependencies with quantities
     */
    public BalancedNode(String operationId, String partId, Map<String, Double> dependencies) {
        this.operationId = operationId;
        this.partId = partId;
        this.dependencies = dependencies;
        this.leftChild = null;
        this.rightChild = null;
        this.nodeHeight = 1; // Initial height of the node
    }
}

/**
 * Represents a balanced tree that uses string-based identifiers for nodes.
 */
public class BalancedTree {
    private BalancedNode rootNode; // Root node of the tree

    /**
     * Adds a new node to the tree.
     *
     * @param operationId  Identifier for the operation
     * @param partId       Identifier for the associated part
     * @param dependencies Map of dependencies with quantities
     */
    public void addNode(String operationId, String partId, Map<String, Double> dependencies) {
        if (operationId == null || partId == null) {
            throw new IllegalArgumentException("Operation ID and Part ID cannot be null.");
        }
        rootNode = addNodeRecursive(rootNode, operationId, partId, dependencies);
    }

    // Recursively adds a node to the tree while maintaining balance
    private BalancedNode addNodeRecursive(BalancedNode current, String operationId, String partId, Map<String, Double> dependencies) {
        if (current == null) {
            System.out.println("Inserting new node: Operation ID = " + operationId + ", Part ID = " + partId);
            return new BalancedNode(operationId, partId, dependencies);
        }

        // If the operation ID is smaller than the current node's operation ID, move left
        if (operationId.compareTo(current.operationId) < 0) {
            System.out.println("Moving to the LEFT: Operation ID = " + operationId + " is less than " + current.operationId);
            current.leftChild = addNodeRecursive(current.leftChild, operationId, partId, dependencies);
        }
        // If the operation ID is greater, move right
        else if (operationId.compareTo(current.operationId) > 0) {
            System.out.println("Moving to the RIGHT: Operation ID = " + operationId + " is greater than " + current.operationId);
            current.rightChild = addNodeRecursive(current.rightChild, operationId, partId, dependencies);
        }
        // If the operation ID is the same, don't insert it again
        else {
            System.out.println("Duplicate Operation ID detected: " + operationId + ". Node insertion skipped.");
            return current; // Duplicate, do not insert
        }

        // Update the height of the current node
        current.nodeHeight = 1 + Math.max(getHeight(current.leftChild), getHeight(current.rightChild));

        // Calculate the balance factor of the node
        int balanceFactor = calculateBalance(current);

        // Perform rotations to balance the tree if necessary
        if (balanceFactor > 1 && operationId.compareTo(current.leftChild.operationId) < 0) {
            System.out.println("Performing RIGHT ROTATION on node: " + current.operationId);
            return rotateRight(current);
        }

        if (balanceFactor < -1 && operationId.compareTo(current.rightChild.operationId) > 0) {
            System.out.println("Performing LEFT ROTATION on node: " + current.operationId);
            return rotateLeft(current);
        }

        if (balanceFactor > 1 && operationId.compareTo(current.leftChild.operationId) > 0) {
            System.out.println("Performing LEFT-RIGHT ROTATION on node: " + current.operationId);
            current.leftChild = rotateLeft(current.leftChild);
            return rotateRight(current);
        }

        if (balanceFactor < -1 && operationId.compareTo(current.rightChild.operationId) < 0) {
            System.out.println("Performing RIGHT-LEFT ROTATION on node: " + current.operationId);
            current.rightChild = rotateRight(current.rightChild);
            return rotateLeft(current);
        }

        return current; // Return the (possibly rebalanced) node
    }

    // Returns the height of a node, or 0 if it's null
    private int getHeight(BalancedNode node) {
        return node == null ? 0 : node.nodeHeight;
    }

    // Calculates the balance factor for a node
    private int calculateBalance(BalancedNode node) {
        return node == null ? 0 : getHeight(node.leftChild) - getHeight(node.rightChild);
    }

    // Performs a left rotation to balance the tree
    private BalancedNode rotateLeft(BalancedNode unbalanced) {
        BalancedNode newRoot = unbalanced.rightChild;
        BalancedNode transferNode = newRoot.leftChild;

        newRoot.leftChild = unbalanced;
        unbalanced.rightChild = transferNode;

        unbalanced.nodeHeight = Math.max(getHeight(unbalanced.leftChild), getHeight(unbalanced.rightChild)) + 1;
        newRoot.nodeHeight = Math.max(getHeight(newRoot.leftChild), getHeight(newRoot.rightChild)) + 1;

        return newRoot;
    }

    // Performs a right rotation to balance the tree
    private BalancedNode rotateRight(BalancedNode unbalanced) {
        BalancedNode newRoot = unbalanced.leftChild;
        BalancedNode transferNode = newRoot.rightChild;

        newRoot.rightChild = unbalanced;
        unbalanced.leftChild = transferNode;

        unbalanced.nodeHeight = Math.max(getHeight(unbalanced.leftChild), getHeight(unbalanced.rightChild)) + 1;
        newRoot.nodeHeight = Math.max(getHeight(newRoot.leftChild), getHeight(newRoot.rightChild)) + 1;

        return newRoot;
    }

    /**
     * Performs an in-order traversal of the tree and processes each node.
     */
    public void traverseInOrder() {
        traverseInOrderRecursive(rootNode);
    }

    // Recursively traverses the tree in in-order (left, node, right)
    private void traverseInOrderRecursive(BalancedNode node) {
        if (node != null) {
            traverseInOrderRecursive(node.leftChild); // Traverse left subtree
            processNode(node); // Process the current node
            traverseInOrderRecursive(node.rightChild); // Traverse right subtree
        }
    }

    // Processes a node by printing its information and dependencies
    private void processNode(BalancedNode node) {
        System.out.println("Operation: " + node.operationId + " | Part: " + node.partId);

        for (Map.Entry<String, Double> entry : node.dependencies.entrySet()) {
            System.out.println("  - Component: " + entry.getKey() + " | Quantity: " + entry.getValue());
        }
    }

    public void printTree() {
        printTreeRecursive(rootNode, "", true);
    }

    // Recursively prints the tree with proper formatting
    private void printTreeRecursive(BalancedNode node, String prefix, boolean isLast) {
        if (node == null) {
            return;
        }

        // Print the current node
        System.out.println(prefix + (isLast ? "└── " : "├── ")
                + (node.operationId != null ? "[" + node.operationId + "]: " : "")
                + node.partId
                + " (Item " + node.partId + ") - Quantity: " + node.dependencies.getOrDefault("Quantity", 1.0));

        // Print dependencies or materials
        if (node.dependencies != null) {
            for (Map.Entry<String, Double> entry : node.dependencies.entrySet()) {
                if (!entry.getKey().equals("Quantity")) {
                    System.out.println(prefix + (isLast ? "    " : "│   ")
                            + "├─ Material: " + entry.getKey());
                }
            }
        }

        // Recurse for children
        if (node.leftChild != null || node.rightChild != null) {
            if (node.leftChild != null) {
                printTreeRecursive(node.leftChild, prefix + (isLast ? "    " : "│   "), node.rightChild == null);
            }
            if (node.rightChild != null) {
                printTreeRecursive(node.rightChild, prefix + (isLast ? "    " : "│   "), true);
            }
        }
    }
}
