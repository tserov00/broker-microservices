package org.example.savingsservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.example.savingsservice.enumeration.CurrencyCodeEnum;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 3)
    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private CurrencyCodeEnum code;

    public Currency() {}

    public Currency(CurrencyCodeEnum code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyCodeEnum getCode() {
        return code;
    }

    public void setCode(CurrencyCodeEnum code) {
        this.code = code;
    }
}
