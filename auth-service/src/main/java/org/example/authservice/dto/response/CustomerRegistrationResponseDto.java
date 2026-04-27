package org.example.authservice.dto.response;

import java.time.LocalDate;

public class CustomerRegistrationResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String passportSeries;
    private String address;
    private String taxId;
    private String phoneNumber;
    private String email;
    private String login;

    public CustomerRegistrationResponseDto(Long id, String firstName, String lastName, LocalDate birthDate,
                                           String passportSeries, String address, String taxId, String phoneNumber,
                                           String email, String login) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.passportSeries = passportSeries;
        this.address = address;
        this.taxId = taxId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public String getAddress() {
        return address;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }
}
