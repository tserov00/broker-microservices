package org.example.orderservice.kafka.event;

import java.util.UUID;

public class AmountReservedEvent {
    private UUID sagaId;
    private UUID operationId;

    private boolean success;
    private String errorMessage;

    public AmountReservedEvent() {}

    public AmountReservedEvent(UUID sagaId, UUID operationId, boolean success, String errorMessage) {
        this.sagaId = sagaId;
        this.operationId = operationId;
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public UUID getSagaId() {
        return sagaId;
    }

    public void setSagaId(UUID sagaId) {
        this.sagaId = sagaId;
    }

    public UUID getOperationId() {
        return operationId;
    }

    public void setOperationId(UUID operationId) {
        this.operationId = operationId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
