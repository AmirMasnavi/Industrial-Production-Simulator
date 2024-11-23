package org.example;

/**
 * Represents a Bill of Materials (BOM) entry, which associates a product
 * with its component parts, their descriptions, and required quantities.
 * <p>
 * This class is immutable, ensuring thread safety and consistent behavior
 * across different contexts.
 */
class BillOfMaterials {

    /**
     * The unique identifier of the product to which this BOM entry belongs.
     */
    private final String productID;

    /**
     * The unique identifier of the part in this BOM entry.
     */
    private final String partNumber;

    /**
     * A brief description of the part, providing additional context or details.
     */
    private final String partDescription;

    /**
     * The quantity of the part required for the associated product.
     */
    private final int quantity;

    /**
     * Constructs a new {@code BillOfMaterials} instance with the specified details.
     *
     * @param productID      the unique identifier of the product; must not be {@code null}.
     * @param partNumber     the unique identifier of the part; must not be {@code null}.
     * @param partDescription a brief description of the part; must not be {@code null}.
     * @param quantity       the quantity of the part required; must be a non-negative integer.
     * @throws IllegalArgumentException if {@code quantity} is negative.
     */
    public BillOfMaterials(String productID, String partNumber, String partDescription, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must not be negative.");
        }
        this.productID = productID;
        this.partNumber = partNumber;
        this.partDescription = partDescription;
        this.quantity = quantity;
    }

    /**
     * Returns the product ID associated with this BOM entry.
     *
     * @return the product ID, never {@code null}.
     */
    public String getProductID() {
        return productID;
    }

    /**
     * Returns the part number of this BOM entry.
     *
     * @return the part number, never {@code null}.
     */
    public String getPartNumber() {
        return partNumber;
    }

    /**
     * Returns the description of the part in this BOM entry.
     *
     * @return the part description, never {@code null}.
     */
    public String getPartDescription() {
        return partDescription;
    }

    /**
     * Returns the quantity of the part required in this BOM entry.
     *
     * @return the quantity, a non-negative integer.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns a string representation of this BOM entry in the format:
     * {@code partNumber - partDescription (Qty: quantity)}.
     * <p>
     * This representation is concise and suitable for logs or simple outputs.
     *
     * @return a string representation of this BOM entry.
     */
    @Override
    public String toString() {
        return partNumber + " - " + partDescription + " (Qty: " + quantity + ")";
    }
}
