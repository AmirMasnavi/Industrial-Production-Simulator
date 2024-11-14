package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Article} class.
 *
 * This class contains a set of JUnit tests for verifying the behavior of the
 * {@link Article} class. It tests various functionalities including retrieving item
 * details, managing operations, and parsing CSV input.
 */
public class ArticleTest {

    private Article article;

    /**
     * Sets up the test environment before each test execution.
     *
     * Initializes a new {@link Article} instance with ID "Item1", priority NORMAL,
     * and a list of operations: "Operation1", "Operation2", and "Operation3".
     */
    @BeforeEach
    public void setup() {
        article = new Article("Item1", Article.Priority.NORMAL, Arrays.asList("Operation1", "Operation2", "Operation3"));
    }

    /**
     * Tests the {@link Article#getIdItem()} method.
     *
     * Verifies that the item ID is correctly returned as "Item1".
     */
    @Test
    public void testGetIdItem() {
        assertEquals("Item1", article.getIdItem(), "Item ID should be 'Item1'");
    }

    /**
     * Tests the {@link Article#getPriority()} method.
     *
     * Verifies that the priority of the item is correctly returned as NORMAL.
     */
    @Test
    public void testGetPriority() {
        assertEquals(Article.Priority.NORMAL, article.getPriority(), "Priority should be NORMAL");
    }

    /**
     * Tests the {@link Article#getNextOperation()} method.
     *
     * Verifies that the next operation is correctly returned as "Operation1".
     */
    @Test
    public void testGetNextOperation() {
        assertEquals("Operation1", article.getNextOperation(), "The next operation should be 'Operation1'");
    }

    /**
     * Tests the {@link Article#moveToNextOperation()} method.
     *
     * Verifies that the item can successfully move to the next operation and that
     * the next operation is updated to "Operation2".
     */
    @Test
    public void testMoveToNextOperation() {
        assertTrue(article.moveToNextOperation(), "Should move to the next operation");
        assertEquals("Operation2", article.getNextOperation(), "The next operation should be 'Operation2'");
    }

    /**
     * Tests the {@link Article#moveToNextOperation()} method when all operations are completed.
     *
     * Moves the item through all available operations and verifies that the method
     * returns false when there are no more operations left.
     */
    @Test
    public void testMoveToNextOperationWhenFinished() {
        article.moveToNextOperation(); // Moves to Operation2
        article.moveToNextOperation(); // Moves to Operation3
        assertFalse(article.moveToNextOperation(), "There should be no more operations after the last one");
    }

    /**
     * Tests the {@link Article#resetOperations()} method.
     *
     * Moves the item to the second operation and then resets the operations.
     * Verifies that the next operation is set back to "Operation1".
     */
    @Test
    public void testResetOperations() {
        article.moveToNextOperation(); // Move to Operation2
        article.resetOperations();
        assertEquals("Operation1", article.getNextOperation(), "After resetting, the next operation should be 'Operation1'");
    }

    /**
     * Tests the {@link Article#fromCSV(String)} method with an invalid priority.
     *
     * Verifies that an {@link IllegalArgumentException} is thrown when attempting
     * to create an item from a CSV line with an invalid priority value.
     */
    @Test
    public void testFromCSV_InvalidPriority() {
        String csvLine = "Item3; InvalidPriority; OperationX";
        assertThrows(IllegalArgumentException.class, () -> {
            Article.fromCSV(csvLine);
        }, "Expected IllegalArgumentException for invalid priority value");
    }

    /**
     * Tests the {@link Article#fromCSV(String)} method with an invalid line format.
     *
     * Verifies that an {@link IllegalArgumentException} is thrown when attempting
     * to create an item from a CSV line with an invalid format (missing fields).
     */
    @Test
    public void testFromCSV_InvalidLineFormat() {
        String csvLine = "Item4; HIGH";
        assertThrows(IllegalArgumentException.class, () -> {
            Article.fromCSV(csvLine);
        }, "Expected IllegalArgumentException for invalid CSV line format");
    }

    /**
     * Tests the {@link Article#toString()} method.
     *
     * Verifies that the string representation of the {@link Article} object matches
     * the expected format, including ID, priority, list of operations, and the
     * current operation index.
     */
    @Test
    public void testToString() {
        String expectedString = "Item{idItem='Item1', priority=NORMAL, operations=[Operation1, Operation2, Operation3]}";
        assertEquals(expectedString, article.toString(), "toString method output should match expected string");
    }
}
