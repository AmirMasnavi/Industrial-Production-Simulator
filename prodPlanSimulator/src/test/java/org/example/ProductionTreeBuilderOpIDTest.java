package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ProductionTreeBuilderOpID class, which is responsible for building a tree structure of production operations.
 * This class tests the methods for handling different scenarios including circular dependencies, missing items, and invalid operations.
 */
class ProductionTreeBuilderOpIDTest {

    private Map<Integer, Map<Integer, Double>> booData;
    private ProductionTreeBuilderOpID builder;

    /**
     * Sets up the test environment before each test method is run.
     * Initializes a list of items, operations, and a booData map with test data.
     */
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

    /**
     * Test case for handling the scenario where the operation ID provided does not correspond to any valid operation.
     * It expects an IllegalArgumentException with a specific message.
     */
    @Test
    void testBuildTreeWithNoAssociatedItem() {
        Map<Integer, Integer> operationToItemMap = new HashMap<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> builder.buildTree(99, operationToItemMap));

        assertEquals("Operation not found for ID: 99", exception.getMessage());
    }

    /**
     * Test case for handling circular dependencies within the production tree.
     * It ensures that the builder avoids infinite recursion and builds a valid tree structure even when a circular dependency exists.
     */
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

    /**
     * Test case for handling the scenario where an operation references a nonexistent item ID.
     * It ensures that the builder throws an IllegalArgumentException when an invalid item ID is encountered.
     */
    @Test
    void testBuildTreeWithMissingItemInOperationMap() {
        Map<Integer, Integer> operationToItemMap = new HashMap<>();
        operationToItemMap.put(10, 1);
        operationToItemMap.put(11, 99); // Nonexistent item ID

        Exception exception = assertThrows(IllegalArgumentException.class, () -> builder.buildTree(11, operationToItemMap));

        assertEquals("Item not found for ID: 99", exception.getMessage());
    }
}
