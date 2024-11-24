package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary Search Tree (BST) for managing materials and their associated quantities.
 * <p>
 * Each node in the tree represents a unique quantity and contains a list of materials
 * associated with that quantity. The tree is organized by quantity, enabling efficient
 * insertion, search, and traversal.
 * </p>
 */
class MaterialBST {

    /**
     * Inner class representing a node in the BST.
     * Each node contains a quantity, a list of associated materials, and pointers to left and right child nodes.
     */
    public static class Node {
        Double quantity; // The quantity associated with this node
        List<String> materials; // List of materials corresponding to the quantity
        Node left, right; // Left and right child nodes

        /**
         * Constructor to initialize a new node with a given quantity and material.
         *
         * @param quantity the quantity to associate with the node
         * @param material the material to add to the node's material list
         */
        Node(Double quantity, String material) {
            this.quantity = quantity;
            this.materials = new ArrayList<>();
            this.materials.add(material);
            this.left = this.right = null;
        }
    }

    private Node root; // Root node of the BST

    /**
     * Inserts a material and its associated quantity into the BST.
     * If the quantity already exists, the material is added to the existing list of materials.
     *
     * @param quantity the quantity to associate with the material
     * @param material the material to insert
     */
    public void insert(Double quantity, String material) {
        root = insertRecursive(root, quantity, material);
    }

    private Node insertRecursive(Node node, Double quantity, String material) {
        if (node == null) {
            return new Node(quantity, material);
        }

        if (quantity < node.quantity) {
            node.left = insertRecursive(node.left, quantity, material);
        } else if (quantity > node.quantity) {
            node.right = insertRecursive(node.right, quantity, material);
        } else {
            node.materials.add(material); // Add material to the existing quantity node
        }

        return node;
    }

    /**
     * Displays materials in ascending order of their quantities.
     */
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

    /**
     * Calculates and displays the total quantity of all materials in the BST.
     * Also prints the total number of materials for each quantity.
     */
    public void displayTotalMaterialsTest() {
        double totalQuantity = displayTotalMaterialsQuantityRecursive(root);
        System.out.println("Total Quantity of Materials Used: " + totalQuantity);
    }

    private double displayTotalMaterialsQuantityRecursive(Node node) {
        double total = 0.0;

        if (node == null) {
            return total;
        }

        total += displayTotalMaterialsQuantityRecursive(node.left);

        int totalMaterialsInNode = node.materials.size();
        System.out.println("Quantity: " + node.quantity
                + ", Materials: " + node.materials
                + "\nTotal Materials in Node: " + totalMaterialsInNode);

        total += node.quantity;

        total += displayTotalMaterialsQuantityRecursive(node.right);

        return total;
    }

    /**
     * Displays materials in descending order of their quantities.
     */
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

    /**
     * Updates the quantity of a specific material in the BST.
     * <p>
     * If the material is found, its associated quantity is updated. The search
     * traverses the tree based on lexicographic order of the material name.
     * </p>
     *
     * @param materialName the name of the material to update
     * @param newQuantity  the new quantity to assign to the material
     */
    public void updateMaterialQuantity(String materialName, double newQuantity) {
        root = updateMaterialRecursive(root, materialName, newQuantity);
    }

    private Node updateMaterialRecursive(Node node, String materialName, double newQuantity) {
        if (node == null) {
            return null; // Material not found
        }

        if (node.materials.contains(materialName)) {
            node.quantity = newQuantity; // Update the quantity
            System.out.println("Updated quantity to: " + newQuantity);
        } else if (materialName.compareTo(node.materials.getFirst()) < 0) {
            node.left = updateMaterialRecursive(node.left, materialName, newQuantity);
        } else {
            node.right = updateMaterialRecursive(node.right, materialName, newQuantity);
        }

        return node;
    }
}
