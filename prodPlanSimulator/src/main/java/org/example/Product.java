package org.example;

/**
 * Represents a product with a unique code, a name, and a description.
 * This class is designed for future use and is currently not integrated into the system.
 *
 * <p>Each product instance is immutable once created, ensuring that
 * the product's attributes remain consistent throughout its lifecycle.</p>
 */
class Product {

    // Fields
    private final String code;         // Unique identifier for the product
    private final String name;         // Name of the product
    private final String description;  // Detailed description of the product

    /**
     * Constructs a new `Product` instance with the given attributes.
     *
     * @param code        the unique code identifying the product
     * @param name        the name of the product
     * @param description a brief description of the product
     */
    public Product(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    /**
     * Retrieves the unique code of the product.
     *
     * @return the product's code
     */
    public String getCode() {
        return code;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return the product's name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the description of the product.
     *
     * @return the product's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Provides a string representation of the product, formatted as:
     * "<code> - <name>: <description>"
     *
     * @return a string representation of the product
     */
    @Override
    public String toString() {
        return code + " - " + name + ": " + description;
    }
}
