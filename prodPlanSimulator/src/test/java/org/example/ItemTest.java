package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    private Item item;

    @BeforeEach
    public void setUp() {
        // This will run before each test, so we set up a new item here.
        item = new Item(1001, "bench leg w/hole");
    }

    @Test
    public void testItemConstructorAndFields() {
        // Test the constructor and field values.
        assertEquals(1001, item.getId());
        assertEquals("bench leg w/hole", item.getName());
    }

    @Test
    public void testItemToString() {
        // Test the toString method.
        String expected = "Item{id=1001, name='bench leg w/hole'}";
        assertEquals(expected, item.toString());
    }

    @Test
    public void testSettersAndGetters() {
        // Test the setters and getters.
        item.setId(2002);
        item.setName("bench seat w/holes");

        assertEquals(2002, item.getId());
        assertEquals("bench seat w/holes", item.getName());
    }

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

