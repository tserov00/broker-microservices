package org.example.orderserivce.client;

import org.example.orderserivce.dto.request.SecurityDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Service
public class SecurityClient {
    private final WebClient marketWebClient;

    public SecurityClient(WebClient marketWebClient) {
        this.marketWebClient = marketWebClient;
    }

    public List<SecurityDto> getByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        return marketWebClient.post()
                .uri("/api/market/securities/batch")
                .bodyValue(ids)
                .retrieve()
                .bodyToFlux(SecurityDto.class)
                .collectList()
                .block();
    }

    public SecurityDto getById(Long id) {
        return marketWebClient.get()
                .uri("/api/market/securities/{id}", id)
                .retrieve()
                .bodyToMono(SecurityDto.class)
                .block();
    }
}
