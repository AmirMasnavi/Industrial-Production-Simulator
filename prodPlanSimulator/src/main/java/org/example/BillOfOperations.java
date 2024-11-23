package org.example;

public class BillOfOperations {
    private final String productCode;
    private final String operationID;
    private final String operationName;
    private final int operationNumber; // operationNumber in minutes

    public BillOfOperations(String productCode, String operationID, String operationName, int operationNumber) {
        this.productCode = productCode;
        this.operationID = operationID;
        this.operationName = operationName;
        this.operationNumber = operationNumber;
    }

    @Override
    public String toString() {
        return operationName + " (Code: " + operationID + ", operationNumber: " + operationNumber + " )";
    }

    // Getters (if needed)
    public String getProductCode() { return productCode; }
    public String getOperationCode() { return operationID; }
    public String getOperationName() { return operationName; }
    public int getoperationNumber() { return operationNumber; }
}
