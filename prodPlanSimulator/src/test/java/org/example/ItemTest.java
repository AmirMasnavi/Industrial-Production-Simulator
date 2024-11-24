package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for testing the functionality of the Item class.
 * <p>
 * This class contains test cases for verifying the behavior of the Item class,
 * including constructor, getter and setter methods, string representation,
 * equality, and hashCode implementation.
 * </p>
 */
public class ItemTest {

    private Item item;

    /**
     * Sets up the test environment by initializing a new Item object before each test case.
     * This method will be executed before each test to ensure a fresh instance of the Item.
     */
    @BeforeEach
    public void setUp() {
        // This will run before each test, so we set up a new item here.
        item = new Item(1001, "bench leg w/hole");
    }

    /**
     * Tests the constructor and field values of the Item class.
     * <p>
     * This test verifies that the Item object is correctly initialized with
     * the provided ID and name.
     * </p>
     */
    @Test
    public void testItemConstructorAndFields() {
        // Test the constructor and field values.
        assertEquals(1001, item.getId());
        assertEquals("bench leg w/hole", item.getName());
    }

    /**
     * Tests the {@code toString} method of the Item class.
     * <p>
     * This test verifies that the {@code toString} method produces the correct
     * string representation of the Item object in the expected format.
     * </p>
     */
    @Test
    public void testItemToString() {
        // Test the toString method.
        String expected = "Item{id=1001, name='bench leg w/hole'}";
        assertEquals(expected, item.toString());
    }

    /**
     * Tests the setter and getter methods of the Item class.
     * <p>
     * This test verifies that the setter methods properly update the fields
     * and the getter methods return the expected values.
     * </p>
     */
    @Test
    public void testSettersAndGetters() {
        // Test the setters and getters.
        item.setId(2002);
        item.setName("bench seat w/holes");

        assertEquals(2002, item.getId());
        assertEquals("bench seat w/holes", item.getName());
    }

    /**
     * Tests equality between two different Item objects.
     * <p>
     * This test verifies that two Item objects are considered equal if their
     * ID and name fields are identical. It also checks the inequality when
     * these fields differ.
     * </p>
     */
    @Test
    public void testItemEquality() {
        // Test equality between two different items.
        Item anotherItem = new Item(1001, "bench leg w/hole");

        // Since the IDs and names are the same, they should be equal.
        assertEquals(item, anotherItem);

        // Modify the second item's name to test inequality
        anotherItem.setName("bench leg w/bolt");
        assertNotEquals(item, anotherItem);
    }

    /**
     * Tests the {@code hashCode} method of the Item class.
     * <p>
     * This test ensures that the hash code of an Item object is consistent with
     * its equality check. Two equal items should have the same hash code.
     * The test also verifies that modifying the name of an item results in a
     * different hash code if the objects are no longer considered equal.
     * </p>
     */
    @Test
    public void testItemHashCode() {
        // Test hashCode consistency.
        Item anotherItem = new Item(1001, "bench leg w/hole");

        // Two equal items should have the same hashCode.
        assertEquals(item.hashCode(), anotherItem.hashCode());

        // Change the name of one item and check if the hash codes are different.
        anotherItem.setName("bench leg w/bolt");
        assertNotEquals(item.hashCode(), anotherItem.hashCode());
    }
}
