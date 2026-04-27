package org.example.authservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_seq")
    @SequenceGenerator(name = "customers_seq", sequenceName = "customers_id_seq", allocationSize = 1)
    Long id;

    @Size(max = 50)
    @Column(name = "first_name")
    String firstName;

    @Size(max = 50)
    @Column(name = "last_name")
    String lastName;

    @Column(name = "date_of_birth")
    LocalDate birthDate;

    @Size(max = 30)
    @Column(name = "passport_series")
    String passportSeries;

    @Size(max = 255)
    @Column(name = "address")
    String address;

    @Size(max = 30)
    @Column(name = "tax_id")
    String taxId;

    public Customer() {}

    public Customer(String firstName, String lastName, LocalDate birthDate, String passportSeries, String address, String taxId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.passportSeries = passportSeries;
        this.address = address;
        this.taxId = taxId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {this.id = id;}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }
}
