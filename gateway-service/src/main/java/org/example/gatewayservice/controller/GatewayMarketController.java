package org.example.gatewayservice.controller;

import org.example.gatewayservice.dto.response.MarketSecuritiesResponseDto;
import org.example.gatewayservice.dto.response.SavingsAccountResponseDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/market")
public class GatewayMarketController {
    private final WebClient marketWebClient;

    public GatewayMarketController(WebClient marketWebClient) {
        this.marketWebClient = marketWebClient;
    }

    @GetMapping("/securities")
    public ResponseEntity<List<MarketSecuritiesResponseDto>> getSecurities() {
        return marketWebClient.get()
                .uri("/api/market/securities")
                .retrieve()
                .toEntityList(MarketSecuritiesResponseDto.class)
                .block();
    }
}
