package org.example;


public class OperationsBST {
    private class Node {
        Operation operation;
        int depth; // Dependency level
        Node left;
        Node right;

        public Node(Operation operation, int depth) {
            this.operation = operation;
            this.depth = depth;
        }
    }

    private Node root;

    // Insert a new operation based on its dependency level
    public void insert(Operation operation, int depth) {
        root = insertRecursive(root, operation, depth);
    }

    private Node insertRecursive(Node current, Operation operation, int depth) {
        if (current == null) {
            return new Node(operation, depth);
        }

        // Compare based on depth (higher depth -> higher priority)
        if (depth > current.depth) {
            current.left = insertRecursive(current.left, operation, depth);
        } else if (depth < current.depth) {
            current.right = insertRecursive(current.right, operation, depth);
        } else {
            // If depths are equal, order by operation ID as a secondary criteria
            if (operation.getId() < current.operation.getId()) {
                current.left = insertRecursive(current.left, operation, depth);
            } else {
                current.right = insertRecursive(current.right, operation, depth);
            }
        }
        return current;
    }

    // In-order traversal to retrieve operations by dependency level
    public void printInOrder() {
        printInOrderRecursive(root);
    }

    private void printInOrderRecursive(Node node) {
        if (node != null) {
            printInOrderRecursive(node.left);
            System.out.println("Operation: " + node.operation.getName() +
                    " (ID: " + node.operation.getId() + ", Depth: " + node.depth + ")");
            printInOrderRecursive(node.right);
        }
    }
}


