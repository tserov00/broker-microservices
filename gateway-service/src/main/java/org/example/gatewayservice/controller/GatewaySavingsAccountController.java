package org.example.gatewayservice.controller;

import jakarta.validation.Valid;
import org.example.gatewayservice.dto.request.SavingsAccountTransactionDto;
import org.example.gatewayservice.dto.response.SavingsAccountHistoryResponseDto;
import org.example.gatewayservice.dto.response.SavingsAccountResponseDto;
import org.example.gatewayservice.security.UserPrincipal;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class GatewaySavingsAccountController {

    private final WebClient savingsWebClient;

    public GatewaySavingsAccountController(WebClient savingsWebClient) {
        this.savingsWebClient = savingsWebClient;
    }

    @GetMapping("/list")
    public ResponseEntity<List<SavingsAccountResponseDto>> getSavingsAccounts(@AuthenticationPrincipal UserPrincipal user) {
        return savingsWebClient.get()
                .uri("/api/accounts/list")
                .header("User-Id", String.valueOf(user.getUserId()))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<SavingsAccountResponseDto>>() {})
                .block();
    }

    @GetMapping("/history")
    public ResponseEntity<List<SavingsAccountHistoryResponseDto>> getHistory(@AuthenticationPrincipal UserPrincipal user) {
        return savingsWebClient.get()
                .uri("/api/accounts/history")
                .header("User-Id", String.valueOf(user.getUserId()))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<SavingsAccountHistoryResponseDto>>() {})
                .block();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@Valid @RequestBody SavingsAccountTransactionDto transactionDto, @AuthenticationPrincipal UserPrincipal user) {
        return savingsWebClient.post()
                .uri("/api/accounts/deposit")
                .header("User-Id", String.valueOf(user.getUserId()))
                .bodyValue(transactionDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@Valid @RequestBody SavingsAccountTransactionDto transactionDto, @AuthenticationPrincipal UserPrincipal user) {
        return savingsWebClient.post()
                .uri("/api/accounts/withdraw")
                .header("User-Id", String.valueOf(user.getUserId()))
                .bodyValue(transactionDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
