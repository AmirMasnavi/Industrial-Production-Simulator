package org.example;

import java.util.ArrayList;
import java.util.List;

public class BooEntry {
    private int itemId;
    private int operationId;
    private List<Subcomponent> subcomponents;

    // Constructor
    public BooEntry(int itemId, int operationId) {
        this.itemId = itemId;
        this.operationId = operationId;
        this.subcomponents = new ArrayList<>();
    }

    // Getters
    public int getItemId() {
        return itemId;
    }

    public int getOperationId() {
        return operationId;
    }

    public List<Subcomponent> getSubcomponents() {
        return subcomponents;
    }

    // Add a subcomponent to the list
    public void addSubcomponent(int subItemId, double quantity) {
        this.subcomponents.add(new Subcomponent(subItemId, quantity));
    }

    // Inner class to represent subcomponent with ID and quantity
    public static class Subcomponent {
        private int subItemId;
        private double quantity;

        public Subcomponent(int subItemId, double quantity) {
            this.subItemId = subItemId;
            this.quantity = quantity;
        }

        public int getSubItemId() {
            return subItemId;
        }

        public double getQuantity() {
            return quantity;
        }
    }

    @Override
    public String toString() {
        return "BooEntry{" +
                "itemId=" + itemId +
                ", operationId=" + operationId +
                ", subcomponents=" + subcomponents +
                '}';
    }
}
