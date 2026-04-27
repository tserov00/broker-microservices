package org.example.orderserivce.controller;

import jakarta.validation.Valid;
import org.example.orderserivce.dto.request.OrderDto;
import org.example.orderserivce.dto.response.OrderResponseDto;
import org.example.orderserivce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/buy")
    public ResponseEntity<Void> createBuyOrder(@Valid @RequestBody OrderDto orderDto, @RequestHeader("User-Id") Long userId) {
        orderService.createBuyOrder(orderDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sell")
    public ResponseEntity<Void> createSellOrder(@Valid @RequestBody OrderDto orderDto, @RequestHeader("User-Id") Long userId) {
        orderService.createSellOrder(orderDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponseDto>> getHistory(@RequestHeader("User-Id") Long userId) {
        List responseBody = orderService.getHistory(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
