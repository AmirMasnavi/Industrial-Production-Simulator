package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for the ProductionTreeNode class, which represents nodes in a production tree.
 * This class tests various functionalities such as constructing nodes, adding children, updating material quantities, and other behaviors.
 */
public class ProductionTreeNodeTest {

    private Item item;
    private Operation operation;
    private ProductionTreeNode itemNode;
    private ProductionTreeNode operationNode;

    /**
     * Sets up the test environment before each test method is executed.
     * Initializes test data for items, operations, and corresponding production tree nodes.
     */
    @BeforeEach
    public void setUp() {
        // Initialize test data
        item = new Item(1001, "bench leg w/hole");
        operation = new Operation(14, "drill bench leg");

        // Initialize nodes
        itemNode = new ProductionTreeNode(item);
        operationNode = new ProductionTreeNode(operation);
    }

    /**
     * Test case for the constructor and fields of ProductionTreeNode.
     * Verifies that the node is properly initialized with item or operation.
     */
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

    /**
     * Test case for adding a child node to a production tree node.
     * Verifies that the child is properly added and can be retrieved.
     */
    @Test
    public void testAddChild() {
        ProductionTreeNode childNode = new ProductionTreeNode(new Item(1002, "bench leg w/bolt"));
        itemNode.addChild(childNode);

        List<ProductionTreeNode> children = itemNode.getChildren();
        assertEquals(1, children.size());
        assertEquals(childNode, children.get(0));
    }

    /**
     * Test case for setters and getters in the ProductionTreeNode.
     * Verifies that the item and operation can be updated using setters.
     */
    @Test
    public void testSettersAndGetters() {
        itemNode.setItem(new Item(1010, "updated bench leg"));
        assertEquals(1010, itemNode.getItem().getId());
        assertEquals("updated bench leg", itemNode.getItem().getName());

        operationNode.setOperation(new Operation(20, "varnish bench"));
        assertEquals(20, operationNode.getOperation().getId());
        assertEquals("varnish bench", operationNode.getOperation().getName());
    }

    /**
     * Test case for the toString method of ProductionTreeNode.
     * Verifies that the string representation of the node is correctly formatted.
     */
    @Test
    public void testProductionTreeNodeToString() {
        String expectedItemNode = "ProductionTreeNode{item=Item{id=1001, name='bench leg w/hole'}}";
        assertEquals(expectedItemNode, itemNode.toString());

        String expectedOperationNode = "ProductionTreeNode{operation=Operation{id=14, name='drill bench leg'}}";
        assertEquals(expectedOperationNode, operationNode.toString());
    }

    /**
     * Test case for updating the material quantity of a root node.
     * Verifies that the root node's quantity can be updated correctly.
     */
    @Test
    public void testUpdateMaterialQuantityRootNodeOnly() {
        // Set initial quantity
        itemNode.setQuantity(10.0);

        // Update quantity
        itemNode.updateMaterialQuantity(20.0);

        // Verify that the quantity was updated correctly
        assertEquals(20.0, itemNode.getQuantity());
    }

    /**
     * Test case for updating the material quantity when there are child nodes.
     * Verifies that the quantity is updated both for the root and its children in proportion.
     */
    @Test
    public void testUpdateMaterialQuantityWithChildren() {
        // Set initial quantity
        itemNode.setQuantity(10.0);
        ProductionTreeNode child1 = new ProductionTreeNode(new Item(1002, "bench top"));
        child1.setQuantity(5.0);

        ProductionTreeNode child2 = new ProductionTreeNode(new Item(1003, "bench screws"));
        child2.setQuantity(2.0);

        itemNode.addChild(child1);
        itemNode.addChild(child2);

        // Update root node's quantity
        itemNode.updateMaterialQuantity(20.0); // New value for the root node

        // Verify that the root node's quantity was updated
        assertEquals(20.0, itemNode.getQuantity());

        // Verify that the children's quantities were updated proportionally
        assertEquals(10.0, child1.getQuantity()); // 5.0 * 2.0 (scaling factor)
        assertEquals(4.0, child2.getQuantity());  // 2.0 * 2.0 (scaling factor)
    }

    /**
     * Test case for updating the material quantity with nested children.
     * Verifies that quantities are updated correctly at all levels of the tree.
     */
    @Test
    public void testUpdateMaterialQuantityWithNestedChildren() {
        // Set initial quantity
        itemNode.setQuantity(10.0);
        ProductionTreeNode child1 = new ProductionTreeNode(new Item(1002, "bench top"));
        child1.setQuantity(5.0);

        ProductionTreeNode child2 = new ProductionTreeNode(new Item(1003, "bench screws"));
        child2.setQuantity(2.0);

        ProductionTreeNode grandChild = new ProductionTreeNode(new Item(1004, "screw nut"));
        grandChild.setQuantity(1.0);

        child2.addChild(grandChild);
        itemNode.addChild(child1);
        itemNode.addChild(child2);

        // Update root node's quantity
        itemNode.updateMaterialQuantity(20.0); // New value for the root node

        // Verify that the root node's quantity was updated
        assertEquals(20.0, itemNode.getQuantity());

        // Verify that the children's quantities were updated proportionally
        assertEquals(10.0, child1.getQuantity());    // 5.0 * 2.0 (scaling factor)
        assertEquals(4.0, child2.getQuantity());     // 2.0 * 2.0 (scaling factor)
        assertEquals(2.0, grandChild.getQuantity()); // 1.0 * 2.0 (scaling factor)
    }

    /**
     * Test case for updating the material quantity when there are no children.
     * Verifies that the quantity is updated correctly when there are no child nodes to scale.
     */
    @Test
    public void testUpdateMaterialQuantityNoChildren() {
        // Set initial quantity
        itemNode.setQuantity(15.0);

        // Update quantity
        itemNode.updateMaterialQuantity(30.0);

        // Verify that the quantity was updated correctly
        assertEquals(30.0, itemNode.getQuantity());
        assertTrue(itemNode.getChildren().isEmpty()); // No children
    }

    /**
     * Test case for updating the material quantity to zero.
     * Verifies that the quantity is set to zero both for the root and all its child nodes.
     */
    @Test
    public void testUpdateMaterialQuantityZeroQuantity() {
        // Set initial quantity
        itemNode.setQuantity(10.0);
        ProductionTreeNode child = new ProductionTreeNode(new Item(1002, "bench top"));
        child.setQuantity(5.0);
        itemNode.addChild(child);

        // Update quantity to zero
        itemNode.updateMaterialQuantity(0.0);

        // Verify that the root node's quantity was set to zero
        assertEquals(0.0, itemNode.getQuantity());

        // Verify that the children's quantities were also set to zero
        assertEquals(0.0, child.getQuantity());
    }

}
