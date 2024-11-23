package org.example;

/**
 * Represents an item with an ID and a name.
 * <p>
 * This class provides a simple structure for storing and managing information about an item,
 * including methods for accessing and modifying its properties, as well as utility methods
 * for comparison and string representation.
 * </p>
 */
public class Item {

    private int id; // Unique identifier for the item
    private String name; // Name of the item

    /**
     * Constructs a new {@code Item} with the specified ID and name.
     *
     * @param id   the unique identifier for the item
     * @param name the name of the item
     */
    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the unique identifier of the item.
     *
     * @return the item's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the item.
     *
     * @param id the new ID to assign to the item
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the item.
     *
     * @return the item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name the new name to assign to the item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the {@code Item}.
     * <p>
     * The string includes the item's ID and name, formatted for readability.
     * </p>
     *
     * @return a string representation of the item
     */
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Compares this {@code Item} to another object for equality.
     * <p>
     * Two {@code Item} objects are considered equal if they have the same ID and name.
     * </p>
     *
     * @param o the object to compare with this {@code Item}
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && name.equals(item.name);
    }

    /**
     * Computes a hash code for this {@code Item}.
     * <p>
     * The hash code is based on the item's ID and name, ensuring consistency
     * with the {@link #equals(Object)} method.
     * </p>
     *
     * @return a hash code value for the item
     */
    @Override
    public int hashCode() {
        return 31 * id + name.hashCode();
    }
}
