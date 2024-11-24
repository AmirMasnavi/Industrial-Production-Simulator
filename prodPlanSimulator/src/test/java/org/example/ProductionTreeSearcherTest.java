package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ProductionTreeSearcher class, which is responsible for indexing and searching nodes in a production tree.
 * This class contains test cases for indexing the tree, searching by name, checking the parent operation, and handling non-existent nodes.
 */
class ProductionTreeSearcherTest {
    private ProductionTreeSearcher searcher;
    private ProductionTreeNode rootNode;

    /**
     * Sets up the test environment before each test method is executed.
     * Initializes a sample production tree with items and operations and indexes it using the ProductionTreeSearcher.
     */
    @BeforeEach
    void setUp() {
        searcher = new ProductionTreeSearcher();

        // Create a sample production tree
        Item item1 = new Item(1, "Item1");
        Item item2 = new Item(2, "Item2");
        Item item3 = new Item(3, "Item3");
        Operation op1 = new Operation(1, "Operation1");

        rootNode = new ProductionTreeNode(item1);
        ProductionTreeNode opNode = new ProductionTreeNode(op1);
        ProductionTreeNode child1 = new ProductionTreeNode(item2);
        ProductionTreeNode child2 = new ProductionTreeNode(item3);

        rootNode.addChild(opNode);
        opNode.addChild(child1);
        opNode.addChild(child2);

        // Index the tree
        searcher.indexTree(rootNode);
    }

    /**
     * Test case for the indexTree method.
     * Verifies that all nodes are indexed correctly by ensuring that searches return valid results.
     */
    @Test
    void testIndexTree() {
        // Ensure all nodes are indexed
        assertNotNull(searcher.search("Item1"));
        assertNotNull(searcher.search("Operation1"));
        assertNotNull(searcher.search("Item2"));
        assertNotNull(searcher.search("Item3"));

        // Ensure IDs are indexed correctly
        assertNotNull(searcher.search("1")); // ID of Item1
        assertNotNull(searcher.search("2")); // ID of Item2
        assertNotNull(searcher.search("3")); // ID of Item3
        assertNotNull(searcher.search("1")); // ID of Operation1
    }

    /**
     * Test case for searching nodes by their name.
     * Verifies that the correct details are returned when searching for a node by its name.
     */
    @Test
    void testSearchByName() {
        // Search by name
        String result = searcher.search("Item1");
        assertTrue(result.contains("Name: Item1"));
        assertTrue(result.contains("ID: 1"));
        assertTrue(result.contains("Type: Material"));

        result = searcher.search("Operation1");
        assertTrue(result.contains("Name: Operation1"));
        assertTrue(result.contains("ID: 1"));
        assertTrue(result.contains("Type: Operation"));
    }

    /**
     * Test case for checking the parent operation when searching for a child node.
     * Verifies that the parent operation of a child node is included in the search result.
     */
    @Test
    void testParentOperationInDetails() {
        // Search a child node and check parent operation
        String result = searcher.search("Item2");
        assertTrue(result.contains("Parent Operation: [Op1] Operation1"));
    }

    /**
     * Test case for handling search results when a node is not found.
     * Verifies that the search returns the appropriate "not found" message when searching for non-existent nodes.
     */
    @Test
    void testSearchNotFound() {
        // Search for a non-existent node
        String result = searcher.search("NonExistentItem");
        assertEquals("=== No results found for: NonExistentItem ===", result);

        result = searcher.search("999");
        assertEquals("=== No results found for: 999 ===", result);
    }
}
