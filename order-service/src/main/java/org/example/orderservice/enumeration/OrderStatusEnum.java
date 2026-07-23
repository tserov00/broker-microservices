package org.example.orderservice.enumeration;

public enum OrderStatusEnum {
    OPEN("OPEN"),
    EXECUTED("EXECUTED"),
    PARTIALLY_EXECUTED("PARTIALLY_EXECUTED"),
    CANCELED("CANCELED");

    private String description;

    OrderStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
