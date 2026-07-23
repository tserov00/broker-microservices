package org.example.orderservice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transactions_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buy_order_id")
    private Order buyOrder;

    @ManyToOne
    @JoinColumn(name = "sell_order_id")
    private Order sellOrder;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "executed_price")
    private BigDecimal executedPrice;

    @Column(name = "executed_fee")
    private BigDecimal executedFee;

    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    public Transaction() {}

    public Transaction(Order buyOrder, Order sellOrder, Integer quantity, BigDecimal executedPrice, BigDecimal executedFee, LocalDateTime executedAt) {
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.quantity = quantity;
        this.executedPrice = executedPrice;
        this.executedFee = executedFee;
        this.executedAt = executedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(Order buyOrder) {
        this.buyOrder = buyOrder;
    }

    public Order getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(Order sellOrder) {
        this.sellOrder = sellOrder;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(BigDecimal executedPrice) {
        this.executedPrice = executedPrice;
    }

    public BigDecimal getExecutedFee() {
        return executedFee;
    }

    public void setExecutedFee(BigDecimal executedFee) {
        this.executedFee = executedFee;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
}
