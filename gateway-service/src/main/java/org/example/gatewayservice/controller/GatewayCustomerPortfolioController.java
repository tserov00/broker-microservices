package org.example.gatewayservice.controller;

import org.example.gatewayservice.dto.response.PortfolioResponseDto;
import org.example.gatewayservice.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class GatewayCustomerPortfolioController {
    private final WebClient portfolioWebClient;

    public GatewayCustomerPortfolioController(WebClient portfolioWebClient) {
        this.portfolioWebClient = portfolioWebClient;
    }

    @GetMapping("/list")
    public ResponseEntity<List<PortfolioResponseDto>> getPortfolio(@AuthenticationPrincipal UserPrincipal user) {
        return portfolioWebClient.get()
                .uri("/api/portfolio/list")
                .header("User-Id", String.valueOf(user.getUserId()))
                .retrieve()
                .toEntityList(PortfolioResponseDto.class)
                .block();
    }
}
