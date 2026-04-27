package org.example.savingsservice.enumeration;

public enum TransactionTypeEnum {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    SENT("SENT"),
    RECEIVED("RECEIVED");

    String description;

    TransactionTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
