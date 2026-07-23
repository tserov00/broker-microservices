package org.example.orderservice.kafka.command;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferMoneyCommand {
    private UUID sagaId;
    private UUID operationId;

    private Long senderId;
    private Long receiverId;
    private Long currencyId;

    private BigDecimal amount;
    private BigDecimal fee;

    public TransferMoneyCommand() {}

    public TransferMoneyCommand(UUID sagaId, UUID operationId, Long senderId, Long receiverId, Long currencyId, BigDecimal amount, BigDecimal fee) {
        this.sagaId = sagaId;
        this.operationId = operationId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.currencyId = currencyId;
        this.amount = amount;
        this.fee = fee;
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

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
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

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
