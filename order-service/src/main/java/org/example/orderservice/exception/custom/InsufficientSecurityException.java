package org.example.orderservice.exception.custom;

public class InsufficientSecurityException extends RuntimeException {
    public InsufficientSecurityException(String message) {
        super(message);
    }
}
