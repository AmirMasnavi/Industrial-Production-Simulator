package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductionTreeBuilderTest {

    private Map<Integer, Map<Integer, Double>> booData;
    private ProductionTreeBuilder builder;

    @BeforeEach
    void setUp() {
        // Initialize items
        List<Item> items = Arrays.asList(
                new Item(1, "Root Item"),
                new Item(2, "Sub Item 1"),
                new Item(3, "Sub Item 2")
        );

        // Initialize operations
        List<Operation> operations = Arrays.asList(
                new Operation(10, "Operation 1"),
                new Operation(11, "Operation 2")
        );

        // Initialize booData (updated to Map<Integer, Map<Integer, Double>>)
        booData = new HashMap<>();
        booData.put(1, Map.of(2, 1.0, 10, 0.5)); // Item 1 has Sub Item 1 and Operation 1 as subcomponents
        booData.put(2, Map.of(3, 2.0)); // Sub Item 1 has Sub Item 2 as its subcomponent

        // Initialize itemQuantities
        Map<Integer, Double> itemQuantities = new HashMap<>();
        itemQuantities.put(1, 2000.0); // Root item quantity
        itemQuantities.put(2, 3000.0); // Sub Item 1 quantity

        // Initialize the builder
        builder = new ProductionTreeBuilder(items, operations, booData, itemQuantities);
    }

    @Test
    void testBuildTreeWithNonExistentItem() {
        Map<Integer, Integer> operationToItemMap = new HashMap<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            builder.buildTree(99, operationToItemMap);
        });

        assertEquals("Item not found for ID: 99", exception.getMessage());
    }

    @Test
    void testBuildTreeWithNoSubComponents() {
        booData.clear(); // No subcomponents for any item

        Map<Integer, Integer> operationToItemMap = new HashMap<>();
        ProductionTreeNode root = builder.buildTree(1, operationToItemMap);

        // Verify the tree contains only the root item
        assertEquals("Root Item", root.getItem().getName());
        assertEquals(2000.0, root.getQuantity());
        assertTrue(root.getChildren().isEmpty());
    }

}
