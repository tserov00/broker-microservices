package org.example.savingsservice.controller;

import org.apache.coyote.Response;
import org.example.savingsservice.dto.response.CurrencyResponseDto;
import org.example.savingsservice.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyResponseDto> getCurrencyById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.getCurrencyById(id));
    }
}
