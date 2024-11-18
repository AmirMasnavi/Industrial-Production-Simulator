package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductionTreeBuilderTest {

    private List<Item> items;
    private List<Operation> operations;
    private Map<Integer, List<int[]>> booData;
    private Map<Integer, Integer> itemQuantities;
    private ProductionTreeBuilder builder;

    @BeforeEach
    void setUp() {
        items = Arrays.asList(
                new Item(1, "Root Item"),
                new Item(2, "Sub Item 1"),
                new Item(3, "Sub Item 2")
        );

        operations = Arrays.asList(
                new Operation(10, "Operation 1"),
                new Operation(11, "Operation 2")
        );

        booData = new HashMap<>();
        booData.put(1, Arrays.asList(new int[]{2, 1000}, new int[]{10, 500}));
        booData.put(2, Arrays.asList(new int[]{3, 2000}));

        itemQuantities = new HashMap<>();
        itemQuantities.put(1, 2000);
        itemQuantities.put(2, 3000);

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
        assertEquals(2.0, root.getQuantity());
        assertTrue(root.getChildren().isEmpty());
    }
}
