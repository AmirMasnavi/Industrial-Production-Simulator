package org.example;

/**
 * Represents an operation entity with a unique identifier and a name.
 * Provides utility methods for managing and comparing instances of operations.
 */
public class Operation {
    // Fields
    /**
     * The unique identifier for the operation.
     */
    private int id;

    /**
     * The name of the operation.
     */
    private String name;

    // Constructor
    /**
     * Constructs a new Operation instance with the specified id and name.
     *
     * @param id   the unique identifier for the operation
     * @param name the name of the operation
     */
    public Operation(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    /**
     * Retrieves the unique identifier of the operation.
     *
     * @return the operation's id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the operation.
     *
     * @param id the new id for the operation
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the operation.
     *
     * @return the operation's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the operation.
     *
     * @param name the new name for the operation
     */
    public void setName(String name) {
        this.name = name;
    }

    // toString method for easier printing
    /**
     * Provides a string representation of the Operation instance,
     * including its id and name.
     *
     * @return a string representation of the operation
     */
    @Override
    public String toString() {
        return "Operation{id=" + id + ", name='" + name + "'}";
    }

    // Equals and hashCode methods
    /**
     * Compares this operation to the specified object for equality.
     * Two operations are considered equal if they have the same id and name.
     *
     * @param o the object to compare with
     * @return {@code true} if the operations are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return id == operation.id && name.equals(operation.name);
    }

    /**
     * Computes the hash code for this operation based on its id and name.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return 31 * id + name.hashCode();
    }
}
