package org.example.orderservice.controller;

import org.example.orderservice.dto.response.PortfolioResponseDto;
import org.example.orderservice.service.CustomerPortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class CustomerPortfolioController {
    private final CustomerPortfolioService customerPortfolioService;

    public CustomerPortfolioController(CustomerPortfolioService customerPortfolioService) {
        this.customerPortfolioService = customerPortfolioService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<PortfolioResponseDto>> getPortfolio(@RequestHeader("User-Id") Long userId) {
        List<PortfolioResponseDto> responseList = customerPortfolioService.getPortfolio(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
