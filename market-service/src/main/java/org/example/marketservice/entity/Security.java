package org.example.marketservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "securities")
public class Security {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "security_seq")
    @SequenceGenerator(name = "security_seq", sequenceName = "securities_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "security_type")
    private SecurityType securityType;

    @Size(max = 20)
    @Column(name = "ticker")
    private String ticker;

    @Size(max = 12)
    @Column(name = "isin")
    private String isin;

    @Size(max = 100)
    @Column(name = "company_name")
    private String companyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_exchange")
    private StockExchange stockExchange;

    @Column(name = "currency_id")
    private Long currencyId;

    @Column(name = "last_price")
    private BigDecimal lastPrice;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Security() {}

    public Security(Long id, SecurityType securityType, String ticker, String isin, String companyName, StockExchange stockExchange, Long currencyId, BigDecimal lastPrice, LocalDateTime updatedAt) {
        this.id = id;
        this.securityType = securityType;
        this.ticker = ticker;
        this.isin = isin;
        this.companyName = companyName;
        this.stockExchange = stockExchange;
        this.currencyId = currencyId;
        this.lastPrice = lastPrice;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public StockExchange getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
