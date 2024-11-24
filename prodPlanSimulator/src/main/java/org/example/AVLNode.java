package org.example;

import java.util.Map;

public class AVLNode {
    int opId;
    int itemId;
    Map<Integer, Double> subcomponents; // Dependencies for this operation
    AVLNode left, right;
    int height;

    public AVLNode(int opId, int itemId, Map<Integer, Double> subcomponents) {
        this.opId = opId;
        this.itemId = itemId;
        this.subcomponents = subcomponents;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}

class AVLTree {
    private AVLNode root;

    public void insert(int opId, int itemId, Map<Integer, Double> subcomponents) {
        root = insert(root, opId, itemId, subcomponents);
    }

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

    private int getHeight(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private AVLNode rotateLeft(AVLNode z) {
        AVLNode y = z.right;
        AVLNode T2 = y.left;

        y.left = z;
        z.right = T2;

        z.height = Math.max(getHeight(z.left), getHeight(z.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

        return y;
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    public void inorderTraversal() {
        inorderTraversal(root);
    }

    private void inorderTraversal(AVLNode node) {
        if (node != null) {
            inorderTraversal(node.left);
            processOperation(node);
            inorderTraversal(node.right);
        }
    }

    private void processOperation(AVLNode node) {
        System.out.println("Processing operation: " + node.opId + " for item: " + node.itemId);

        for (Map.Entry<Integer, Double> entry : node.subcomponents.entrySet()) {
            System.out.println("  - Subcomponent: Item " + entry.getKey() + " with quantity " + entry.getValue());
        }
    }
}
