package org.example;

public class Order {
    private final String orderId;
    private final String productId;
    private final int quantity;
    private final String priority;

    public Order(String orderId, String productId, String priority, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.priority = priority;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }
    public String getPriority(){
        return priority;
    }

    public int getQuantity() {
        return quantity;
    }
}

