package org.example.marketservice.controller;

import org.example.marketservice.dto.response.MarketSecuritiesResponseDto;
import org.example.marketservice.service.MarketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/securities")
    public ResponseEntity<List<MarketSecuritiesResponseDto>> getSecurities() {
        return ResponseEntity.status(HttpStatus.OK).body(marketService.getSecurities());
    }

    @PostMapping("/securities/batch")
    public ResponseEntity<List<MarketSecuritiesResponseDto>> getSecuritiesBatchById(@RequestBody List<Long> ids) {
        return ResponseEntity.status(HttpStatus.OK).body(marketService.getSecuritiesBatchByIds(ids));
    }

    @GetMapping("/securities/{id}")
    public ResponseEntity<MarketSecuritiesResponseDto> getSecuritiesById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(marketService.getSecurityById(id));
    }
}
