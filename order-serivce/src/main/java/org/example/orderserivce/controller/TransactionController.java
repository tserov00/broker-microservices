package org.example.orderserivce.controller;

import org.example.orderserivce.dto.response.TransactionResponseDto;
import org.example.orderserivce.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(("/api/transactions"))
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponseDto>> getTransactions(@RequestHeader("User-Id") Long userId) {
        List<TransactionResponseDto> responseList = transactionService.getAllTransactions(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
