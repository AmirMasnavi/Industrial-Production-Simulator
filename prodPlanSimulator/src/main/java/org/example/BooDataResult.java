package org.example;

import java.util.*;

public class BooDataResult {
    public Map<Integer, List<int[]>> booData;
    public Map<Integer, Integer> itemQuantities;

    public BooDataResult(Map<Integer, List<int[]>> booData, Map<Integer, Integer> itemQuantities) {
        this.booData = booData;
        this.itemQuantities = itemQuantities;
    }
}
