package org.example.gatewayservice.controller;

import org.example.gatewayservice.dto.response.TransactionResponseDto;
import org.example.gatewayservice.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class GatewayTransactionController {
    private final WebClient transactionWebClient;

    public GatewayTransactionController(WebClient transactionWebClient) {
        this.transactionWebClient = transactionWebClient;
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponseDto>> getTransactions(@AuthenticationPrincipal UserPrincipal user) {
        return transactionWebClient.get()
                .uri("/api/transactions/history")
                .header("User-Id", String.valueOf(user.getUserId()))
                .retrieve()
                .toEntityList(TransactionResponseDto.class)
                .block();
    }
}
