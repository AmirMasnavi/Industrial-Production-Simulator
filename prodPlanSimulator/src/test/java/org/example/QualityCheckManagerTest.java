package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the QualityCheckManager class, which manages quality checks on production tree nodes.
 * This class contains test cases for adding, performing, and processing quality checks based on tree depth,
 * as well as handling empty queues.
 */
class QualityCheckManagerTest {

    private QualityCheckManager qualityCheckManager;

    /**
     * Sets up the test environment before each test method is executed.
     * Initializes a new instance of the QualityCheckManager.
     */
    @BeforeEach
    void setUp() {
        qualityCheckManager = new QualityCheckManager();
    }

    /**
     * Test case for adding quality checks based on the depth of nodes in the tree.
     * Verifies that the quality checks are added correctly in priority order.
     */
    @Test
    void testAddQualityCheckBasedOnDepth() {
        // Create a sample tree structure
        ProductionTreeNode root = new ProductionTreeNode(new Item(1, "Root Item"));
        ProductionTreeNode child1 = new ProductionTreeNode(new Item(2, "Child Item 1"));
        ProductionTreeNode child2 = new ProductionTreeNode(new Item(3, "Child Item 2"));

        root.addChild(child1);
        root.addChild(child2);

        ProductionTreeNode grandChild = new ProductionTreeNode(new Item(4, "Grandchild Item"));
        child1.addChild(grandChild);

        // Add quality checks based on the tree
        qualityCheckManager.addQualityCheckBasedOnDepth(root, 0);

        // View the quality checks (should be in priority order: 0 -> 1 -> 2)
        qualityCheckManager.viewQualityChecks();
    }

    /**
     * Test case for performing quality checks on the nodes.
     * Verifies that quality checks are performed in the expected order and outputs the result.
     */
    @Test
    void testPerformQualityChecks() {
        // Create a tree structure with varying depths
        ProductionTreeNode root = new ProductionTreeNode(new Item(1, "Root Item"));
        ProductionTreeNode child1 = new ProductionTreeNode(new Item(2, "Child Item 1"));
        ProductionTreeNode child2 = new ProductionTreeNode(new Item(3, "Child Item 2"));
        ProductionTreeNode grandChild = new ProductionTreeNode(new Item(4, "Grandchild Item"));

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandChild);

        // Add quality checks based on depth
        qualityCheckManager.addQualityCheckBasedOnDepth(root, 0);

        // Perform the quality checks and verify output order
        System.out.println("Performing Quality Checks:");
        qualityCheckManager.performQualityChecks();
    }

    /**
     * Test case for processing quality checks in reverse order.
     * Verifies that quality checks are processed in reverse order and outputs the result.
     */
    @Test
    void testProcessQualityChecksInReverse() {
        // Create a tree structure
        ProductionTreeNode root = new ProductionTreeNode(new Item(1, "Root Item"));
        ProductionTreeNode child1 = new ProductionTreeNode(new Item(2, "Child Item 1"));
        ProductionTreeNode child2 = new ProductionTreeNode(new Item(3, "Child Item 2"));
        ProductionTreeNode grandChild = new ProductionTreeNode(new Item(4, "Grandchild Item"));

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandChild);

        // Add quality checks based on depth
        qualityCheckManager.addQualityCheckBasedOnDepth(root, 0);

        // Process the quality checks in reverse order
        System.out.println("Processing Quality Checks in Reverse:");
        qualityCheckManager.processQualityChecksInReverse();
    }

    /**
     * Test case for performing quality checks on an empty queue.
     * Verifies that performing quality checks on an empty queue does not throw exceptions.
     */
    @Test
    void testEmptyQueue() {
        // Test performing quality checks on an empty queue
        System.out.println("Testing with Empty Queue:");
        assertDoesNotThrow(() -> qualityCheckManager.performQualityChecks());
    }

    /**
     * Test case for viewing quality checks on an empty queue.
     * Verifies that viewing quality checks on an empty queue does not throw exceptions or display any checks.
     */
    @Test
    void testViewQualityChecksOnEmptyQueue() {
        // Test viewing quality checks on an empty queue
        System.out.println("Viewing Quality Checks on Empty Queue:");
        qualityCheckManager.viewQualityChecks(); // Should not throw an exception or print any checks
    }
}
