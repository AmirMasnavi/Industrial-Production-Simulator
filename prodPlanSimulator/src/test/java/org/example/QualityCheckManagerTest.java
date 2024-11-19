package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class QualityCheckManagerTest {

    private QualityCheckManager qualityCheckManager;

    @BeforeEach
    void setUp() {
        qualityCheckManager = new QualityCheckManager();
    }

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

    @Test
    void testEmptyQueue() {
        // Test performing quality checks on an empty queue
        System.out.println("Testing with Empty Queue:");
        assertDoesNotThrow(() -> qualityCheckManager.performQualityChecks());
    }

    @Test
    void testViewQualityChecksOnEmptyQueue() {
        // Test viewing quality checks on an empty queue
        System.out.println("Viewing Quality Checks on Empty Queue:");
        qualityCheckManager.viewQualityChecks(); // Should not throw an exception or print any checks
    }
}
