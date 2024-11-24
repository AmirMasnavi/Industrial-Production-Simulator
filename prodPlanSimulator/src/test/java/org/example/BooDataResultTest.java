package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test class for testing the functionality of the BooDataResult class.
 * <p>
 * This class contains various test cases for verifying the behavior of the BooDataResult class,
 * specifically the updateItemQuantity method and its impact on the booData and itemQuantities structures.
 * </p>
 */
class BooDataResultTest {

    private BooDataResult booDataResult;

    /**
     * Sets up the test environment by initializing the BooDataResult object
     * with mock data for booData and itemQuantities before each test case.
     */
    @BeforeEach
    void setUp() {
        // Initial setup for BooDataResult object
        Map<Integer, Map<Integer, Double>> booData = new HashMap<>();
        Map<Integer, Double> subOperation1 = new HashMap<>();
        subOperation1.put(101, 5.0);
        subOperation1.put(102, 10.0);

        Map<Integer, Double> subOperation2 = new HashMap<>();
        subOperation2.put(201, 20.0);

        booData.put(1, subOperation1);
        booData.put(2, subOperation2);

        Map<Integer, Double> itemQuantities = new HashMap<>();
        itemQuantities.put(101, 5.0);
        itemQuantities.put(102, 10.0);
        itemQuantities.put(201, 20.0);

        booDataResult = new BooDataResult(booData, itemQuantities);
    }

    /**
     * Tests updating an existing item in the itemQuantities map and ensures
     * that the update is reflected correctly in the itemQuantities map.
     */
    @Test
    void testUpdateExistingItemQuantityInItemQuantities() {
        // Update an existing item in the itemQuantities map
        booDataResult.updateItemQuantity(101, 7.5);

        // Verify that the update is reflected in the itemQuantities map
        assertEquals(7.5, booDataResult.getItemQuantities().get(101));
    }

    /**
     * Tests updating an existing item that is a sub-operation in the booData map.
     * Verifies that the update is reflected in the correct location within booData.
     */
    @Test
    void testUpdateExistingItemQuantityInBooDataAsChild() {
        // Update an existing item that is a sub-operation in booData
        booDataResult.updateItemQuantity(102, 15.0);

        // Verify that the update is reflected in booData
        assertEquals(15.0, booDataResult.getBooData().get(1).get(102));
    }

    /**
     * Tests updating an existing item that is a parent operation in the booData map.
     * Verifies that the update is reflected in the correct location within booData.
     */
    @Test
    void testUpdateExistingItemQuantityInBooDataAsParent() {
        // Update an existing item that is a parent operation in booData
        booDataResult.updateItemQuantity(2, 25.0);

        // Verify that the update is reflected in booData
        assertEquals(25.0, booDataResult.getBooData().get(2).get(201));
    }

    /**
     * Tests attempting to update a non-existing item. Verifies that the update has no effect,
     * and the original values remain unchanged.
     */
    @Test
    void testUpdateNonExistingItem() {
        // Try to update a non-existing item
        booDataResult.updateItemQuantity(999, 50.0);

        // Verify that nothing was changed for the non-existing item
        assertEquals(null, booDataResult.getItemQuantities().get(999));
        assertEquals(5.0, booDataResult.getItemQuantities().get(101)); // Original value remains
    }

    /**
     * Tests that updates to items propagate correctly across both the itemQuantities and booData structures.
     * Verifies that the updated item quantity is reflected in both data structures.
     */
    @Test
    void testUpdatePropagatesCorrectlyAcrossDataStructures() {
        // Update an item and check that the changes propagate
        booDataResult.updateItemQuantity(201, 30.0);

        // Verify the update in itemQuantities
        assertEquals(30.0, booDataResult.getItemQuantities().get(201));

        // Verify the update in booData
        assertEquals(30.0, booDataResult.getBooData().get(2).get(201));
    }
}
