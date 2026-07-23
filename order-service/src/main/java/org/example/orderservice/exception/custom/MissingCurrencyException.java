package org.example.orderservice.exception.custom;

public class MissingCurrencyException extends RuntimeException {
    public MissingCurrencyException(String message) {
        super(message);
    }
}
