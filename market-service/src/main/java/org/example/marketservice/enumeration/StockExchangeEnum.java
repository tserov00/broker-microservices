package org.example.marketservice.enumeration;

public enum StockExchangeEnum {
    NYSE("NYSE"),
    NASDAQ("NASDAQ"),
    EN("EN"),
    MOEX("MOEX"),
    LSE("LSE");

    private String description;

    StockExchangeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
