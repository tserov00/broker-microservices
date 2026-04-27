package org.example.savingsservice.exception.custom;

public class MissingCurrencyException extends RuntimeException {
    public MissingCurrencyException(String message) {
        super(message);
    }
}
