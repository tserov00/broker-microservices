package org.example.orderservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class OrderDto {

    private Long securityId;

    @NotNull(message = "Цена не может быть пустой")
    @Positive(message = "Цена должна быть больше 0")
    private BigDecimal price;

    @NotNull(message = "Количество не может быть пустым")
    @Positive(message = "Количество должно быть больше нуля")
    private Integer quantity;

    public OrderDto() {
    }

    public OrderDto(Long securityId, BigDecimal price, Integer quantity) {
        this.securityId = securityId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getSecurityId() {
        return securityId;
    }

    public void setSecurityId(Long securityId) {
        this.securityId = securityId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
