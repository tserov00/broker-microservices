package org.example.gatewayservice.controller;


import jakarta.validation.Valid;
import org.example.gatewayservice.dto.request.CustomerAuthenticationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/auth")
public class GatewayAuthenticationController {
    private final WebClient authWebClient;

    public GatewayAuthenticationController(WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateCustomer(@Valid @RequestBody CustomerAuthenticationDto customer) {
        return authWebClient.post()
                .uri("/api/auth/login")
                .bodyValue(customer)
                .retrieve()
                .toEntity(String.class)
                .block();
    }
}
