package org.example.savingsservice.kafka.command;

import java.math.BigDecimal;
import java.util.UUID;

public class ReserveAmountCommand {
    private UUID sagaId;
    private UUID operationId;

    private Long userId;
    private Long currencyId;
    private BigDecimal amount;

    public ReserveAmountCommand() {}

    public ReserveAmountCommand(UUID sagaId, UUID operationId, Long userId, Long currencyId, BigDecimal amount) {
        this.sagaId = sagaId;
        this.operationId = operationId;
        this.userId = userId;
        this.currencyId = currencyId;
        this.amount = amount;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
