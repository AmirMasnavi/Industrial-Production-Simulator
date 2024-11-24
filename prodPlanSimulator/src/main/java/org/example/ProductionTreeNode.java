package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in a production tree, which can either hold an {@link Item} or an {@link Operation}.
 * Each node can have multiple child nodes, forming a hierarchical structure.
 * The node also tracks quantities and operations associated with the production process.
 */
public class ProductionTreeNode {
    private Item item; // The item associated with this node, if applicable
    private Operation operation; // The operation associated with this node, if applicable
    private final List<ProductionTreeNode> children = new ArrayList<>(); // List of child nodes
    private double quantity; // Quantity associated with this node
    private Operation parentOperation; // The parent operation of this node, if applicable

    private double totalMaterialQuantity = 0.0; // Aggregated quantity of materials used in this subtree

    /**
     * Constructor to create a node representing an {@link Item}.
     *
     * @param item The {@link Item} to associate with this node.
     */
    public ProductionTreeNode(Item item) {
        this.item = item;
    }

    /**
     * Constructor to create a node representing an {@link Operation}.
     *
     * @param operation The {@link Operation} to associate with this node.
     */
    public ProductionTreeNode(Operation operation) {
        this.operation = operation;
    }

    /**
     * Retrieves the {@link Item} associated with this node.
     *
     * @return The associated {@link Item}, or null if this node represents an operation.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets the {@link Item} associated with this node.
     *
     * @param item The {@link Item} to associate with this node.
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Retrieves the {@link Operation} associated with this node.
     *
     * @return The associated {@link Operation}, or null if this node represents an item.
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Sets the {@link Operation} associated with this node.
     *
     * @param operation The {@link Operation} to associate with this node.
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * Retrieves the quantity associated with this node.
     *
     * @return The quantity for this node.
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity associated with this node.
     *
     * @param quantity The quantity to set.
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Adds a child node to this node.
     *
     * @param child The child {@link ProductionTreeNode} to add.
     */
    public void addChild(ProductionTreeNode child) {
        children.add(child);
    }

    /**
     * Retrieves the list of child nodes for this node.
     *
     * @return A list of child {@link ProductionTreeNode}s.
     */
    public List<ProductionTreeNode> getChildren() {
        return children;
    }

    /**
     * Retrieves the parent {@link Operation} of this node.
     *
     * @return The parent {@link Operation}, or null if not applicable.
     */
    public Operation getParentOperation() {
        return parentOperation;
    }

    /**
     * Sets the parent {@link Operation} of this node.
     *
     * @param parentOperation The parent {@link Operation} to set.
     */
    public void setParentOperation(Operation parentOperation) {
        this.parentOperation = parentOperation;
    }

    /**
     * Retrieves the ID of the {@link Item} associated with this node.
     *
     * @return The ID of the associated {@link Item}, or -1 if no item is associated.
     */
    public int getItemId() {
        return item != null ? item.getId() : -1;
    }

    /**
     * Retrieves the name of the {@link Item} associated with this node.
     *
     * @return The name of the associated {@link Item}, or "Unknown Item" if no item is associated.
     */
    public String getItemName() {
        return item != null ? item.getName() : "Unknown Item";
    }

    /**
     * Recursively calculates and displays the total quantity of materials used by each operation in the subtree.
     *
     * @param node The root node of the subtree to process.
     */
    private void displayMaterialsByOperationRecursive(ProductionTreeNode node) {
        if (node == null) {
            return;
        }

        if (node.getOperation() != null) {
            double totalQuantity = 0.0;

            for (ProductionTreeNode child : node.getChildren()) {
                totalQuantity += child.getQuantity();
            }

            totalMaterialQuantity += totalQuantity;

            System.out.println("Operation: " + node.getOperation().getName() +
                    ", Total Material Quantity Used: " + totalQuantity);
        }

        // Recurse over child nodes
        for (ProductionTreeNode child : node.getChildren()) {
            displayMaterialsByOperationRecursive(child);
        }
    }

    /**
     * Calculates and displays the total quantity of materials used in the entire subtree.
     *
     * @param node The root node of the subtree to process.
     */
    public void displayTotalMaterials(ProductionTreeNode node) {
        totalMaterialQuantity = 0.0;

        displayMaterialsByOperationRecursive(node);

        System.out.println("\nTotal Quantity of Materials Used: " + totalMaterialQuantity);
    }

    /**
     * Updates the quantity of this node and recursively scales the quantities of its child nodes proportionally.
     *
     * @param newQuantity The new quantity to set for this node.
     */
    public void updateMaterialQuantity(double newQuantity) {
        double scaleFactor = newQuantity / this.quantity; // Calculate the scale factor
        this.quantity = newQuantity; // Update the current node's quantity

        // Update quantities for all child nodes based on the scale factor
        for (ProductionTreeNode child : children) {
            double updatedQuantity = child.getQuantity() * scaleFactor;
            child.updateMaterialQuantity(updatedQuantity);
        }
    }

    /**
     * Provides a string representation of this node, including its item, operation, and quantity (if applicable).
     *
     * @return A string representation of the node.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProductionTreeNode{");
        if (item != null) {
            sb.append("item=").append(item);
        }
        if (operation != null) {
            sb.append("operation=").append(operation);
        }
        if (quantity != 0) {
            sb.append(", quantity=").append(quantity); // Display quantity if present
        }
        sb.append("}");
        return sb.toString();
    }
}
