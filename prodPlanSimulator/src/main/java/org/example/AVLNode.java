
package org.example;

import java.util.Map;

/**
 * Represents a node in an AVL tree, storing an operation ID, associated item ID,
 * subcomponent dependencies, pointers to left and right child nodes, and the node's height.
 */
public class AVLNode {
    int opId; // Operation ID
    int itemId; // Item ID associated with this operation
    Map<Integer, Double> subcomponents; // Dependencies for this operation
    AVLNode left, right; // Pointers to the left and right child nodes
    int height; // Height of the node in the AVL tree

    /**
     * Constructs a new AVLNode with the given operation ID, item ID, and subcomponents.
     *
     * @param opId         the ID of the operation
     * @param itemId       the ID of the associated item
     * @param subcomponents a map of subcomponent IDs to their quantities
     */
    public AVLNode(int opId, int itemId, Map<Integer, Double> subcomponents) {
        this.opId = opId;
        this.itemId = itemId;
        this.subcomponents = subcomponents;
        this.left = null;
        this.right = null;
        this.height = 1; // Default height for a newly created node
    }
}

/**
 * Represents an AVL tree, a self-balancing binary search tree, supporting operations
 * such as insertion, traversal, and balancing through rotations.
 */
class AVLTree {
    private AVLNode root; // Root node of the AVL tree

    /**
     * Inserts a new node with the given operation ID, item ID, and subcomponents into the tree.
     *
     * @param opId         the ID of the operation
     * @param itemId       the ID of the associated item
     * @param subcomponents a map of subcomponent IDs to their quantities
     */
    public void insert(int opId, int itemId, Map<Integer, Double> subcomponents) {
        root = insert(root, opId, itemId, subcomponents);
    }

    /**
     * Recursively inserts a new node into the tree and ensures balance is maintained.
     *
     * @param node         the current node being evaluated
     * @param opId         the ID of the operation to insert
     * @param itemId       the ID of the associated item
     * @param subcomponents a map of subcomponent IDs to their quantities
     * @return the updated node after insertion and balancing
     */
    private AVLNode insert(AVLNode node, int opId, int itemId, Map<Integer, Double> subcomponents) {
        if (node == null) return new AVLNode(opId, itemId, subcomponents);

        if (opId < node.opId) {
            node.left = insert(node.left, opId, itemId, subcomponents);
        } else if (opId > node.opId) {
            node.right = insert(node.right, opId, itemId, subcomponents);
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balance = getBalance(node);

        if (balance > 1 && opId < node.left.opId) return rotateRight(node);
        if (balance < -1 && opId > node.right.opId) return rotateLeft(node);
        if (balance > 1 && opId > node.left.opId) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && opId < node.right.opId) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Calculates the height of a given node.
     *
     * @param node the node to evaluate
     * @return the height of the node, or 0 if the node is null
     */
    private int getHeight(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    /**
     * Calculates the balance factor of a given node.
     *
     * @param node the node to evaluate
     * @return the balance factor, which is the height difference between left and right subtrees
     */
    private int getBalance(AVLNode node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    /**
     * Performs a left rotation on the given node to restore balance.
     *
     * @param z the node to rotate
     * @return the new root after the rotation
     */
    private AVLNode rotateLeft(AVLNode z) {
        AVLNode y = z.right;
        AVLNode T2 = y.left;

        y.left = z;
        z.right = T2;

        z.height = Math.max(getHeight(z.left), getHeight(z.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

        return y;
    }

    /**
     * Performs a right rotation on the given node to restore balance.
     *
     * @param y the node to rotate
     * @return the new root after the rotation
     */
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    /**
     * Performs an in-order traversal of the tree, processing each node.
     */
    public void inorderTraversal() {
        inorderTraversal(root);
    }

    /**
     * Recursively performs an in-order traversal of the tree.
     *
     * @param node the current node being visited
     */
    private void inorderTraversal(AVLNode node) {
        if (node != null) {
            inorderTraversal(node.left);
            processOperation(node);
            inorderTraversal(node.right);
        }
    }

    /**
     * Processes a node during traversal, printing its operation details.
     *
     * @param node the node to process
     */
    private void processOperation(AVLNode node) {
        System.out.println("Processing operation: " + node.opId + " for item: " + node.itemId);

        for (Map.Entry<Integer, Double> entry : node.subcomponents.entrySet()) {
            System.out.println("  - Subcomponent: Item " + entry.getKey() + " with quantity " + entry.getValue());
        }
    }
}