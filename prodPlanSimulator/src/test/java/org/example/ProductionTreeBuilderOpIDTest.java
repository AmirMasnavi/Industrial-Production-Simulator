package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductionTreeBuilderOpIDTest {

    private Map<Integer, Map<Integer, Double>> booData;
    private ProductionTreeBuilderOpID builder;

    @BeforeEach
    void setUp() {
        List<Item> items = Arrays.asList(
                new Item(1, "Root Item"),
                new Item(2, "Sub Item 1"),
                new Item(3, "Sub Item 2")
        );

        List<Operation> operations = Arrays.asList(
                new Operation(10, "Operation 1"),
                new Operation(11, "Operation 2"),
                new Operation(12, "Operation 3")
        );

        booData = new HashMap<>();
        booData.put(1, Map.of(2, 1000.0, 11, 500.0));
        booData.put(2, Map.of(3, 2000.0));

        Map<Integer, Double> itemQuantities = new HashMap<>();
        itemQuantities.put(1, 2000.0);
        itemQuantities.put(2, 3000.0);
        itemQuantities.put(3, 1500.0);

        builder = new ProductionTreeBuilderOpID(items, operations, booData, itemQuantities);
    }

    @Test
    void testBuildTreeWithNoAssociatedItem() {
        Map<Integer, Integer> operationToItemMap = new HashMap<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> builder.buildTree(99, operationToItemMap));

        assertEquals("Operation not found for ID: 99", exception.getMessage());
    }

    @Test
    void testBuildTreeWithCircularDependency() {
        booData.put(3, Map.of(1, 500.0)); // Circular dependency: Sub Item 2 references Root Item

        Map<Integer, Integer> operationToItemMap = new HashMap<>();
        operationToItemMap.put(10, 1);
        operationToItemMap.put(11, 2);
        operationToItemMap.put(12, 3);

        ProductionTreeNode root = builder.buildTree(10, operationToItemMap);

        // Verify that the builder avoids infinite recursion
        assertNotNull(root);

        // Verify the structure (no duplicates or infinite loops)
        List<ProductionTreeNode> children = root.getChildren();
        assertEquals(1, children.size());
    }


    @Test
    void testBuildTreeWithMissingItemInOperationMap() {
        Map<Integer, Integer> operationToItemMap = new HashMap<>();
        operationToItemMap.put(10, 1);
        operationToItemMap.put(11, 99); // Nonexistent item ID

        Exception exception = assertThrows(IllegalArgumentException.class, () -> builder.buildTree(11, operationToItemMap));

        assertEquals("Item not found for ID: 99", exception.getMessage());
    }
}
