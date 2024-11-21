package org.example;

import java.util.*;

public class BooDataResult {
    public Map<Integer, Map<Integer, Double>> booData;
    public Map<Integer, Double> itemQuantities;

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
