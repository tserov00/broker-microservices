package org.example.orderservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponseDto {
    private Long id;
    private String ticker;
    private String orderType;
    private BigDecimal price;
    private BigDecimal currentPrice;
    private BigDecimal fee;
    private Integer quantity;
    private Integer availableQuantity;
    private String orderStatus;
    private LocalDateTime createdAt;

    public OrderResponseDto() {}

    public OrderResponseDto(Long id, String ticker, String orderType, BigDecimal price, BigDecimal currentPrice, BigDecimal fee, Integer quantity, Integer availableQuantity, String orderStatus, LocalDateTime createdAt) {
        this.id = id;
        this.ticker = ticker;
        this.orderType = orderType;
        this.price = price;
        this.currentPrice = currentPrice;
        this.fee = fee;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
