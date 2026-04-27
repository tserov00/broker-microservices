package org.example.gatewayservice.controller;

import jakarta.validation.Valid;
import org.example.gatewayservice.dto.request.CustomerRegistrationDto;
import org.example.gatewayservice.dto.response.CustomerRegistrationResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/auth")
public class GatewayRegistrationController {
    private final WebClient authWebClient;

    public GatewayRegistrationController(WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerRegistrationDto customer) {
        return authWebClient.post()
                .uri("/api/auth/register")
                .bodyValue(customer)
                .retrieve()
                .toEntity(CustomerRegistrationResponseDto.class)
                .block();
    }
}
