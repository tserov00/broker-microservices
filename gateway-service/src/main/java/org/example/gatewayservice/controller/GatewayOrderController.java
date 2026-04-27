package org.example.gatewayservice.controller;

import org.example.gatewayservice.dto.request.OrderDto;
import org.example.gatewayservice.dto.response.OrderResponseDto;
import org.example.gatewayservice.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class GatewayOrderController {
    private final WebClient orderWebClient;

    public GatewayOrderController(WebClient orderWebClient) {
        this.orderWebClient = orderWebClient;
    }

    @PostMapping("/buy")
    public ResponseEntity<Void> createBuyOrder(@RequestBody OrderDto order, @AuthenticationPrincipal UserPrincipal user) {
        return orderWebClient.post()
                .uri("/api/orders/buy")
                .header("User-Id", String.valueOf(user.getUserId()))
                .bodyValue(order)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @PostMapping("/sell")
    public ResponseEntity<Void> createSellOrder(@RequestBody OrderDto order, @AuthenticationPrincipal UserPrincipal user) {
        return orderWebClient.post()
                .uri("/api/orders/sell")
                .header("User-Id", String.valueOf(user.getUserId()))
                .bodyValue(order)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponseDto>> getHistory(@AuthenticationPrincipal UserPrincipal user) {
        return orderWebClient.get()
                .uri("/api/orders/history")
                .header("User-Id", String.valueOf(user.getUserId()))
                .retrieve()
                .toEntityList(OrderResponseDto.class)
                .block();
    }
}
