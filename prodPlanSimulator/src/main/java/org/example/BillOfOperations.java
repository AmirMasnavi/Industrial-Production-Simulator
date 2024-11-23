package org.example;

/**
 * Represents a Bill of Operations (BOO) entry, which associates a product
 * with its required operations, their descriptions, and their respective durations.
 * <p>
 * This class is immutable, ensuring consistent and thread-safe behavior.
 */
public class BillOfOperations {

    /**
     * The unique identifier for the product to which this BOO entry belongs.
     */
    private final String productCode;

    /**
     * The unique identifier for the operation in this BOO entry.
     */
    private final String operationID;

    /**
     * The name or description of the operation.
     */
    private final String operationName;

    /**
     * The duration of the operation, measured in minutes.
     */
    private final int operationNumber;

    /**
     * Constructs a new {@code BillOfOperations} instance with the specified details.
     *
     * @param productCode     the unique identifier of the product; must not be {@code null}.
     * @param operationID     the unique identifier of the operation; must not be {@code null}.
     * @param operationName   the name or description of the operation; must not be {@code null}.
     * @param operationNumber the duration of the operation in minutes; must be a non-negative integer.
     * @throws IllegalArgumentException if {@code operationNumber} is negative.
     */
    public BillOfOperations(String productCode, String operationID, String operationName, int operationNumber) {
        if (operationNumber < 0) {
            throw new IllegalArgumentException("Operation duration (operationNumber) must not be negative.");
        }
        this.productCode = productCode;
        this.operationID = operationID;
        this.operationName = operationName;
        this.operationNumber = operationNumber;
    }

    /**
     * Returns the product code associated with this BOO entry.
     *
     * @return the product code, never {@code null}.
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Returns the operation ID associated with this BOO entry.
     *
     * @return the operation ID, never {@code null}.
     */
    public String getOperationCode() {
        return operationID;
    }

    /**
     * Returns the name or description of the operation.
     *
     * @return the operation name, never {@code null}.
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * Returns the duration of the operation in minutes.
     *
     * @return the operation duration, a non-negative integer.
     */
    public int getoperationNumber() {
        return operationNumber;
    }

    /**
     * Returns a string representation of this BOO entry in the format:
     * {@code operationName (Code: operationID, operationNumber: duration)}.
     * <p>
     * This representation is concise and suitable for logs or simple outputs.
     *
     * @return a string representation of this BOO entry.
     */
    @Override
    public String toString() {
        return operationName + " (Code: " + operationID + ", operationNumber: " + operationNumber + " )";
    }
}
