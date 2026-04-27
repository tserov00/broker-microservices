package org.example.savingsservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "processed_operations")
public class ProcessedOperation {

    @Id
    private UUID operationId;

    public ProcessedOperation() {}

    public ProcessedOperation(UUID operationId) {
        this.operationId = operationId;
    }

    public UUID getOperationId() {
        return operationId;
    }

    public void setOperationId(UUID operationId) {
        this.operationId = operationId;
    }
}
