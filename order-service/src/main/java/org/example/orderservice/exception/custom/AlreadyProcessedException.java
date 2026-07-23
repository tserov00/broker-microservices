package org.example.orderservice.exception.custom;

public class AlreadyProcessedException extends RuntimeException {
    public AlreadyProcessedException(String message) {
        super(message);
    }
}
