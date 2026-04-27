package org.example.marketservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.example.marketservice.enumeration.StockExchangeEnum;

@Entity
@Table(name = "stock_exchanges")
public class StockExchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Enumerated(EnumType.STRING)
    @Column(name = "stock_exchange")
    private StockExchangeEnum stockExchangeName;

    public StockExchange() {
    }

    public StockExchange(StockExchangeEnum stockExchangeName) {
        this.stockExchangeName = stockExchangeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockExchangeEnum getStockExchangeName() {
        return stockExchangeName;
    }

    public void setStockExchangeName(StockExchangeEnum stockExchangeName) {
        this.stockExchangeName = stockExchangeName;
    }
}
