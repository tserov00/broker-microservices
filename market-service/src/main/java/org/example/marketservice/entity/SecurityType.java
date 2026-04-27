package org.example.marketservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.example.marketservice.enumeration.SecurityTypeEnum;


@Entity
@Table(name = "security_types")
public class SecurityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 10)
    @Enumerated(EnumType.STRING)
    @Column(name = "security_type")
    private SecurityTypeEnum securityType;

    public SecurityType() {
    }

    public SecurityType(SecurityTypeEnum securityType) {
        this.securityType = securityType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SecurityTypeEnum getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecurityTypeEnum securityType) {
        this.securityType = securityType;
    }
}
