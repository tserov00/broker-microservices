package org.example.orderserivce.dto.response;

import java.math.BigDecimal;

public class TransferResponseDto {
    private Long senderId;
    private Long receiverId;
    private Long currencyId;
    private BigDecimal transferAmount;
    private BigDecimal fee;

    public TransferResponseDto() {}

    public TransferResponseDto(Long senderId, Long receiverId, Long currencyId, BigDecimal transferAmount, BigDecimal fee) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.currencyId = currencyId;
        this.transferAmount = transferAmount;
        this.fee = fee;
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

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
