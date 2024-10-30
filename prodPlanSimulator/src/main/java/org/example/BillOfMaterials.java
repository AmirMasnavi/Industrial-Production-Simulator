package org.example;

class BillOfMaterials {

    //Only to be used in the next Sprint
    private final String productID;
    private final String partNumber;
    private final String partDescription;
    private final int quantity;

    public BillOfMaterials(String productID, String partNumber, String partDescription, int quantity) {
        this.productID = productID;
        this.partNumber = partNumber;
        this.partDescription = partDescription;
        this.quantity = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return partNumber + " - " + partDescription + " (Qty: " + quantity + ")";
    }
}
