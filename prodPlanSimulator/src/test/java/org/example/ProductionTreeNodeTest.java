package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ProductionTreeNodeTest {

    private Item item;
    private Operation operation;
    private ProductionTreeNode itemNode;
    private ProductionTreeNode operationNode;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        item = new Item(1001, "bench leg w/hole");
        operation = new Operation(14, "drill bench leg");

        // Initialize nodes
        itemNode = new ProductionTreeNode(item);
        operationNode = new ProductionTreeNode(operation);
    }

    @Test
    public void testProductionTreeNodeConstructorAndFields() {
        assertNotNull(itemNode);
        assertNotNull(operationNode);

        // Test the item node
        assertEquals(item, itemNode.getItem());
        assertNull(itemNode.getOperation());

        // Test the operation node
        assertEquals(operation, operationNode.getOperation());
        assertNull(operationNode.getItem());
    }

    @Test
    public void testAddChild() {
        ProductionTreeNode childNode = new ProductionTreeNode(new Item(1002, "bench leg w/bolt"));
        itemNode.addChild(childNode);

        List<ProductionTreeNode> children = itemNode.getChildren();
        assertEquals(1, children.size());
        assertEquals(childNode, children.get(0));
    }

    @Test
    public void testSettersAndGetters() {
        itemNode.setItem(new Item(1010, "updated bench leg"));
        assertEquals(1010, itemNode.getItem().getId());
        assertEquals("updated bench leg", itemNode.getItem().getName());

        operationNode.setOperation(new Operation(20, "varnish bench"));
        assertEquals(20, operationNode.getOperation().getId());
        assertEquals("varnish bench", operationNode.getOperation().getName());
    }

    @Test
    public void testProductionTreeNodeToString() {
        String expectedItemNode = "ProductionTreeNode{item=Item{id=1001, name='bench leg w/hole'}}";
        assertEquals(expectedItemNode, itemNode.toString());

        String expectedOperationNode = "ProductionTreeNode{operation=Operation{id=14, name='drill bench leg'}}";
        assertEquals(expectedOperationNode, operationNode.toString());
    }
}
