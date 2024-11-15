package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ProductionTreeBuilderTest {

    private ProductionTreeBuilder builder;
    private List<Item> items;
    private List<Operation> operations;
    private Map<Integer, List<int[]>> booData;
    private Map<Integer, Integer> operationToItemMap;

    @BeforeEach
    void setUp() {
        // Setup mock data for items
        items = new ArrayList<>();
        Item item1 = new Item(1, "Item1");
        Item item2 = new Item(2, "Item2");
        Item item3 = new Item(3, "Item3");
        items.add(item1);
        items.add(item2);
        items.add(item3);

        // Setup mock data for operations
        operations = new ArrayList<>();
        Operation op1 = new Operation(1, "Operation1");
        operations.add(op1);

        // Setup BOO data (subcomponents)
        booData = new HashMap<>();
        booData.put(1, Arrays.asList(new int[]{2, 5000}, new int[]{3, 3000}));

        // Setup operation to item map
        operationToItemMap = new HashMap<>();
        operationToItemMap.put(op1.getId(), item1.getId());

        builder = new ProductionTreeBuilder(items, operations, booData);
    }

    @Test
    void testBuildTree() {
        ProductionTreeNode rootNode = builder.buildTree(1, operationToItemMap);

        // Check if the root node is the expected item
        assertEquals("Item1", rootNode.getItem().getName());

        // Check if the child nodes exist as expected
        List<ProductionTreeNode> children = rootNode.getChildren();
        assertEquals(3, children.size());  // One operation node and two subcomponent nodes

        assertEquals("Operation1", children.get(0).getOperation().getName());  // Operation node
        assertEquals("Item2", children.get(1).getItem().getName());  // Subcomponent 1
        assertEquals("Item3", children.get(2).getItem().getName());  // Subcomponent 2
    }
}
