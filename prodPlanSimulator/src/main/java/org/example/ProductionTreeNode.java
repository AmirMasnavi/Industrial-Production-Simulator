package org.example;

import java.util.ArrayList;
import java.util.List;

public class ProductionTreeNode {
    private Item item;
    private Operation operation;
    private final List<ProductionTreeNode> children = new ArrayList<>();
    private double quantity; // Store quantity as a field
    private Operation parentOperation;

    // Constructor for Item
    public ProductionTreeNode(Item item) {
        this.item = item;
    }

    // Constructor for Operation
    public ProductionTreeNode(Operation operation) {
        this.operation = operation;
    }

    // Getter and setter for item
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    // Getter and setter for operation
    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    // Getter and setter for quantity
    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    // Add child node
    public void addChild(ProductionTreeNode child) {
        children.add(child);
    }

    // Get children nodes
    public List<ProductionTreeNode> getChildren() {
        return children;
    }

    public Operation getParentOperation() {
        return parentOperation;
    }

    public void setParentOperation(Operation parentOperation) {
        this.parentOperation = parentOperation;
    }

    // Methods to get item ID and item name
    public int getItemId() {
        return item != null ? item.getId() : -1; // Return -1 if item is null
    }

    public String getItemName() {
        return item != null ? item.getName() : "Unknown Item"; // Return "Unknown Item" if item is null
    }

    public void updateQuantity(double newQuantity) {
        // Update the quantity of the current node
        this.quantity = newQuantity;
    }


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
