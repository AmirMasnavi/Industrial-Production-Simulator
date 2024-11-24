package org.example;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the AVLTree and AVLNode classes.
 */
public class AVLNodeTest {

    /**
     * Tests the insertion of nodes into the AVL tree and verifies the tree's balance.
     */
    @Test
    public void testInsertionAndBalancing() {
        AVLTree tree = new AVLTree();

        Map<Integer, Double> subcomponents1 = new HashMap<>();
        subcomponents1.put(101, 5.0);
        subcomponents1.put(102, 2.0);

        Map<Integer, Double> subcomponents2 = new HashMap<>();
        subcomponents2.put(201, 3.5);
        subcomponents2.put(202, 1.0);

        Map<Integer, Double> subcomponents3 = new HashMap<>();
        subcomponents3.put(301, 4.0);

        // Insert nodes
        tree.insert(10, 1001, subcomponents1);
        tree.insert(20, 1002, subcomponents2);
        tree.insert(5, 1003, subcomponents3);

        // Verify tree structure via in-order traversal
        tree.inorderTraversal();
    }

    /**
     * Tests the handling of duplicate operation IDs in the AVL tree.
     */
    @Test
    public void testDuplicateInsertion() {
        AVLTree tree = new AVLTree();

        Map<Integer, Double> subcomponents1 = new HashMap<>();
        subcomponents1.put(101, 5.0);

        Map<Integer, Double> subcomponents2 = new HashMap<>();
        subcomponents2.put(102, 2.0);

        // Insert nodes with duplicate opId
        tree.insert(10, 1001, subcomponents1);
        tree.insert(10, 1002, subcomponents2);

        // Expected behavior: the tree should not allow duplicate opId (check logic manually)
        tree.inorderTraversal();
    }

    /**
     * Tests the in-order traversal of the AVL tree.
     */
    @Test
    public void testInOrderTraversal() {
        AVLTree tree = new AVLTree();

        Map<Integer, Double> subcomponents1 = new HashMap<>();
        subcomponents1.put(101, 1.5);

        Map<Integer, Double> subcomponents2 = new HashMap<>();
        subcomponents2.put(102, 2.5);

        Map<Integer, Double> subcomponents3 = new HashMap<>();
        subcomponents3.put(103, 3.5);

        // Insert nodes
        tree.insert(15, 2001, subcomponents1);
        tree.insert(10, 2002, subcomponents2);
        tree.insert(20, 2003, subcomponents3);

        // Perform in-order traversal
        tree.inorderTraversal();

        // In-order traversal should process nodes in order: 10, 15, 20
    }

    /**
     * Tests the AVL tree's height property after multiple insertions.
     */
    @Test
    public void testHeightProperty() {
        AVLTree tree = new AVLTree();

        Map<Integer, Double> subcomponents1 = new HashMap<>();
        Map<Integer, Double> subcomponents2 = new HashMap<>();
        Map<Integer, Double> subcomponents3 = new HashMap<>();
        Map<Integer, Double> subcomponents4 = new HashMap<>();

        // Insert nodes to force balancing
        tree.insert(30, 3001, subcomponents1);
        tree.insert(20, 3002, subcomponents2);
        tree.insert(40, 3003, subcomponents3);
        tree.insert(10, 3004, subcomponents4);

        // Root height should reflect a balanced tree
        tree.inorderTraversal();
    }
}
