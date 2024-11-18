package org.example;

import java.util.ArrayList;
import java.util.List;

class MaterialBST {
    // Node class to represent each node in the BST
    private static class Node {
        int quantity;
        List<String> materials; // List of materials associated with this quantity
        Node left, right;

        Node(int quantity, String material) {
            this.quantity = quantity;
            this.materials = new ArrayList<>();
            this.materials.add(material);
            this.left = this.right = null;
        }
    }

    private Node root; // Root node of the BST

    // Insert a material with its quantity into the BST
    public void insert(int quantity, String material) {
        root = insertRecursive(root, quantity, material);
    }

    private Node insertRecursive(Node node, int quantity, String material) {
        if (node == null) {
            return new Node(quantity, material);
        }

        if (quantity < node.quantity) {
            node.left = insertRecursive(node.left, quantity, material);
        } else if (quantity > node.quantity) {
            node.right = insertRecursive(node.right, quantity, material);
        } else {
            // Quantity already exists, add material to the list
            node.materials.add(material);
        }

        return node;
    }

    // Display materials in increasing order of quantity
    public void displayInOrder() {
        System.out.println("Materials in Increasing Order of Quantity:");
        displayInOrderRecursive(root);
    }

    private void displayInOrderRecursive(Node node) {
        if (node != null) {
            displayInOrderRecursive(node.left);
            System.out.println("Quantity: " + node.quantity + ", Materials: " + node.materials);
            displayInOrderRecursive(node.right);
        }
    }

    // Display materials in decreasing order of quantity
    public void displayInReverseOrder() {
        System.out.println("Materials in Decreasing Order of Quantity:");
        displayInReverseOrderRecursive(root);
    }

    private void displayInReverseOrderRecursive(Node node) {
        if (node != null) {
            displayInReverseOrderRecursive(node.right);
            System.out.println("Quantity: " + node.quantity + ", Materials: " + node.materials);
            displayInReverseOrderRecursive(node.left);
        }
    }

    public static void main(String[] args) {
        MaterialBST bst = new MaterialBST();

        // Sample data
        bst.insert(10, "Steel");
        bst.insert(5, "Plastic");
        bst.insert(20, "Aluminum");
        bst.insert(10, "Iron");
        bst.insert(15, "Copper");
        bst.insert(5, "Rubber");

        // Display materials in increasing order
        bst.displayInOrder();

        // Display materials in decreasing order
        bst.displayInReverseOrder();
    }
}
