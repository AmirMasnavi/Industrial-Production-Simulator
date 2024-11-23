package org.example;

import java.util.*;

/**
 * Represents the result data structure for Bill of Operations (BOO) calculations.
 * <p>
 * This class holds data about operations and item quantities, organizing them
 * in a structured way for further processing or reporting.
 */
public class BooDataResult {

    /**
     * A map where the key is an operation ID, and the value is another map associating
     * sub-operation IDs with their respective double values (e.g., duration or cost).
     */
    public Map<Integer, Map<Integer, Double>> booData;

    /**
     * A map where the key is an item ID, and the value is the quantity of that item.
     */
    public Map<Integer, Double> itemQuantities;

    /**
     * Constructs a new {@code BooDataResult} instance with the specified data.
     *
     * @param booData        a nested map representing operation data.
     * @param itemQuantities a map representing quantities of items.
     */
    public BooDataResult(Map<Integer, Map<Integer, Double>> booData, Map<Integer, Double> itemQuantities) {
        this.booData = booData;
        this.itemQuantities = itemQuantities;
    }

    public Map<Integer, Map<Integer, Double>> getBooData() {
        return booData;
    }

    public Map<Integer, Double> getItemQuantities() {
        return itemQuantities;
    }

    public void updateItemQuantity(int itemId, double newQuantity) {

        // Update the itemQuantities map
        if (itemQuantities.containsKey(itemId)) {
            itemQuantities.put(itemId, newQuantity);
        }

        // Update booData where this item ID exists as a child or parent
        if (booData.containsKey(itemId)) {
            for (Map.Entry<Integer, Double> entry : booData.get(itemId).entrySet()) {
                entry.setValue(newQuantity);
            }
        }

        for (Map.Entry<Integer, Map<Integer, Double>> entry : booData.entrySet()) {
            if (entry.getValue().containsKey(itemId)) {
                entry.getValue().put(itemId, newQuantity);
            }
        }

        System.out.println("Updated booData: " + booData);

    }
}
