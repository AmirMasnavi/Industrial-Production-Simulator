package org.example;

import java.util.*;

public class BooDataResult {
    public Map<Integer, Map<Integer, Double>> booData;
    public Map<Integer, Double> itemQuantities;

    public BooDataResult(Map<Integer, Map<Integer, Double>> booData, Map<Integer, Double> itemQuantities) {
        this.booData = booData;
        this.itemQuantities = itemQuantities;
    }
}
