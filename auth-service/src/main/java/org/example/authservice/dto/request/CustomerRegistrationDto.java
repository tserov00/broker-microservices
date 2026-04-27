package org.example.authservice.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class CustomerRegistrationDto {
    @NotBlank
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов")
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    @Size(min = 1, max = 30, message = "Номер и серия паспорта должны содержать до 30 символов")
    private String passportSeries;

    @Size(max = 255, message = "Адрес должен содержать менее 255 символов")
    private String address;

    @NotBlank
    @Size(min = 1, max = 30, message = "Номер налогоплательщика должен содержать до 30 символов")
    private String taxId;

    @NotBlank
    @Pattern(
            regexp = "\\+?\\d{10,15}",
            message = "Телефон должен содержать от 10 до 15 цифр, можно с +"
    )
    private String phoneNumber;

    @NotBlank
    @Email
    @Size(min = 6, max = 100, message = "Электронная почта должна содержать от 6 до 100 символов")
    private String email;

    @NotBlank
    @Size(min = 3, max = 50, message = "Логин должен содержать от 3 до 50 символов")
    private String login;

    @NotBlank
    private String password;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
