package org.example;

import java.util.ArrayList;
import java.util.List;

class MaterialBST {
    // Node class to represent each node in the BST
    public static class Node {
        Double quantity;
        List<String> materials; // List of materials associated with this quantity
        Node left, right;

        Node(Double quantity, String material) {
            this.quantity = quantity;
            this.materials = new ArrayList<>();
            this.materials.add(material);
            this.left = this.right = null;
        }
    }

    private Node root; // Root node of the BST

    // Insert a material with its quantity into the BST
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

    public void displayTotalMaterialsTest(){
        displayTotalMaterialsQuantityRecursive(root);
    }

    public double displayTotalMaterialsQuantityRecursive(Node node) {
        if (node == null) {
            return 0.0; // Base case: no quantity in a null node
        }

        // Traverse left, process current node, and then traverse right
        double leftSum = displayTotalMaterialsQuantityRecursive(node.left);

        // Calculate total materials for the current operation
        int totalMaterialsInNode = node.materials.size();
        System.out.println("Quantity: " + node.quantity
                + ", Materials: " + node.materials
                + "\nTotal Materials in Node: " + totalMaterialsInNode);

        double rightSum = displayTotalMaterialsQuantityRecursive(node.right);

        // Accumulate the sum of quantities
        return leftSum + node.quantity + rightSum;

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

    // Method to update the quantity of a material in the BST
    public void updateMaterialQuantity(String materialName, double newQuantity) {
        root = updateMaterialRecursive(root, materialName, newQuantity);
    }

    private Node updateMaterialRecursive(Node node, String materialName, double newQuantity) {
        if (node == null) {
            return null; // Material not found
        }


        // Find the material in the node's list
        if (node.materials.contains(materialName)) {
            // Update the material's quantity
            node.quantity = newQuantity;
            System.out.println(newQuantity);
        } else if (materialName.compareTo(node.materials.get(0)) < 0) {
            // Search left if the material name is lexicographically smaller
            node.left = updateMaterialRecursive(node.left, materialName, newQuantity);
        } else {
            // Search right if the material name is lexicographically larger
            node.right = updateMaterialRecursive(node.right, materialName, newQuantity);
        }

        return node;
    }


}
