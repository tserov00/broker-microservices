package org.example.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerAuthenticationDto {

    @NotBlank(message = "Логин обязятелен")
    @Size(max = 50, message = "Неверный логин или пароль")
    private String login;

    @NotBlank(message = "Пароль обязателен")
    private String password;

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
