package org.example;

import java.util.ArrayList;
import java.util.List;

public class ProductionTreeNode {
    private Item item;          // The item at this node
    private Operation operation; // The operation at this node
    private List<ProductionTreeNode> children; // List of child nodes

    // Constructor for an item node
    public ProductionTreeNode(Item item) {
        this.item = item;
        this.children = new ArrayList<>();
    }

    // Constructor for an operation node
    public ProductionTreeNode(Operation operation) {
        this.operation = operation;
        this.children = new ArrayList<>();
    }

    // Add child node
    public void addChild(ProductionTreeNode child) {
        this.children.add(child);
    }

    // Getters and Setters
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public List<ProductionTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<ProductionTreeNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProductionTreeNode{");
        boolean hasItem = item != null;
        boolean hasOperation = operation != null;

        if (hasItem) {
            sb.append("item=").append(item);
        }

        if (hasItem && hasOperation) {
            sb.append(", ");
        }

        if (hasOperation) {
            sb.append("operation=").append(operation);
        }

        sb.append("}");
        return sb.toString();
    }

}


