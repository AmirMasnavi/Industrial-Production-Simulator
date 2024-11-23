package org.example;

/**
 * A binary search tree (BST) implementation for managing `Operation` objects based on their dependency levels.
 * This structure ensures operations are organized hierarchically, with priority determined by their depth
 * (dependency level) and operation ID as a secondary criterion.
 */
public class OperationsBST {

    // Inner class representing a node in the BST
    private class Node {
        Operation operation; // The operation stored in this node
        int depth; // Dependency level of the operation
        Node left; // Pointer to the left child node
        Node right; // Pointer to the right child node

        /**
         * Constructs a new `Node` instance with the given operation and depth.
         *
         * @param operation the operation associated with this node
         * @param depth     the dependency level of the operation
         */
        public Node(Operation operation, int depth) {
            this.operation = operation;
            this.depth = depth;
        }
    }

    private Node root; // Root node of the BST

    /**
     * Inserts a new `Operation` into the BST based on its dependency level.
     * Operations with higher dependency levels are prioritized and placed higher in the tree.
     * If two operations have the same depth, they are ordered by their ID.
     *
     * @param operation the `Operation` to be added
     * @param depth     the dependency level of the operation
     */
    public void insert(Operation operation, int depth) {
        root = insertRecursive(root, operation, depth);
    }

    /**
     * Recursive helper method for inserting a new node into the BST.
     *
     * @param current   the current node being evaluated
     * @param operation the operation to insert
     * @param depth     the dependency level of the operation
     * @return the updated node after insertion
     */
    private Node insertRecursive(Node current, Operation operation, int depth) {
        if (current == null) {
            return new Node(operation, depth);
        }

        // Compare depth to determine placement in the tree
        if (depth > current.depth) {
            current.left = insertRecursive(current.left, operation, depth);
        } else if (depth < current.depth) {
            current.right = insertRecursive(current.right, operation, depth);
        } else {
            // If depths are equal, use operation ID as a tie-breaker
            if (operation.getId() < current.operation.getId()) {
                current.left = insertRecursive(current.left, operation, depth);
            } else {
                current.right = insertRecursive(current.right, operation, depth);
            }
        }
        return current;
    }

    /**
     * Performs an in-order traversal of the BST, printing each operation
     * in order of their dependency levels and IDs.
     * This provides a sorted view of the operations.
     */
    public void printInOrder() {
        printInOrderRecursive(root);
    }

    /**
     * Recursive helper method for in-order traversal of the BST.
     * Traverses the left subtree, processes the current node, and then traverses the right subtree.
     *
     * @param node the current node being processed
     */
    private void printInOrderRecursive(Node node) {
        if (node != null) {
            printInOrderRecursive(node.left);
            System.out.println("Operation: " + node.operation.getName() +
                    " (ID: " + node.operation.getId() + ", Depth: " + node.depth + ")");
            printInOrderRecursive(node.right);
        }
    }
}
