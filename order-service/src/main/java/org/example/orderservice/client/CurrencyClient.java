package org.example.orderservice.client;

import org.example.orderservice.dto.request.CurrencyDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CurrencyClient {
    private final WebClient savingsWebClient;

    public CurrencyClient(WebClient savingsWebClient) {
        this.savingsWebClient = savingsWebClient;
    }

    public CurrencyDto getCurrencyById(Long id) {
        return savingsWebClient.get()
                .uri("/api/currency/{id}", id)
                .retrieve()
                .bodyToMono(CurrencyDto.class)
                .block();
    }
}
