package org.example.orderserivce.saga;

public enum SagaStep {
    START,
    MONEY_TRANSFERRED,
    RESERVE_RELEASED,
    SECURITY_TRANSFERRED,
    FINALIZE,
    COMPLETED,
    FAILED,
    ROLLBACK_STARTED,
    ROLLBACK_COMPLETED,
}
