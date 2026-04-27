package org.example.savingsservice.controller;



import jakarta.validation.Valid;

import org.example.savingsservice.dto.request.ReserveAmountRequestDto;
import org.example.savingsservice.dto.request.SavingsAccountTransactionDto;
import org.example.savingsservice.dto.request.TransferRequestDto;
import org.example.savingsservice.dto.response.SavingsAccountEntityResponseDto;
import org.example.savingsservice.dto.response.SavingsAccountHistoryResponseDto;
import org.example.savingsservice.dto.response.SavingsAccountResponseDto;
import org.example.savingsservice.service.SavingsAccountsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class SavingsAccountsController {

    private final SavingsAccountsService savingsAccountsService;

    public SavingsAccountsController(SavingsAccountsService savingsAccountsService) {
        this.savingsAccountsService = savingsAccountsService;
    }


    @GetMapping("/list")
    public ResponseEntity<List<SavingsAccountResponseDto>> getSavingsAccounts(@RequestHeader("User-Id") Long userId) {
        List<SavingsAccountResponseDto> savingsAccounts =
                savingsAccountsService.getSavingsAccounts(userId);

        return ResponseEntity.ok(savingsAccounts);
    }

    @GetMapping("/history")
    public ResponseEntity<List<SavingsAccountHistoryResponseDto>> getHistory(@RequestHeader("User-Id") Long userId) {
        List<SavingsAccountHistoryResponseDto> history =
                savingsAccountsService.getHistory(userId);

        return ResponseEntity.ok(history);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@Valid @RequestBody SavingsAccountTransactionDto transactionDto, @RequestHeader("User-Id") Long userId) {
        savingsAccountsService.deposit(userId, transactionDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@Valid @RequestBody SavingsAccountTransactionDto transactionDto, @RequestHeader("User-Id") Long userId) {
        savingsAccountsService.withdraw(userId, transactionDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<SavingsAccountEntityResponseDto> getByCurrency(@RequestParam Long currencyId ,@RequestHeader("User-Id") Long userId) {
        SavingsAccountEntityResponseDto responseDto = savingsAccountsService.getSavingsAccount(currencyId, userId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserveAmount(@RequestBody Map<String, Object> body, @RequestHeader("X-Request-Id") UUID requestId) {
        Long userId = body.get("userId") != null ? Long.valueOf(body.get("userId").toString()) : null;
        Long currencyId = body.get("currencyId") != null ? Long.valueOf(body.get("currencyId").toString()) : null;
        BigDecimal amount = body.get("amount") != null ? new BigDecimal(body.get("amount").toString()) : null;

        if (userId == null || currencyId == null || amount == null) {
            return ResponseEntity.badRequest().build();
        }

        savingsAccountsService.reserveAmount(userId, currencyId, amount, requestId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/release")
    public ResponseEntity<Void> releaseAmount(@RequestBody Map<String, Object> body, @RequestHeader("X-Request-Id") UUID requestId) {
        Long userId = body.get("userId") != null ? Long.valueOf(body.get("userId").toString()) : null;
        Long currencyId = body.get("currencyId") != null ? Long.valueOf(body.get("currencyId").toString()) : null;
        BigDecimal amount = body.get("amount") != null ? new BigDecimal(body.get("amount").toString()) : null;

        if (userId == null || currencyId == null || amount == null) {
            return ResponseEntity.badRequest().build();
        }

        savingsAccountsService.releaseAmount(userId, currencyId, amount, requestId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferAmount(@RequestBody Map<String, Object> body, @RequestHeader("X-Request-Id") UUID requestId) {
        Long senderId = body.get("senderId") != null ? Long.valueOf(body.get("senderId").toString()) : null;
        Long receiverId = body.get("receiverId") != null ? Long.valueOf(body.get("receiverId").toString()) : null;
        Long currencyId = body.get("currencyId") != null ? Long.valueOf(body.get("currencyId").toString()) : null;
        BigDecimal transferAmount = body.get("transferAmount") != null
                ? new BigDecimal(body.get("transferAmount").toString()) : null;
        BigDecimal fee = body.get("fee") != null
                ? new BigDecimal(body.get("fee").toString()) : null;

        if (senderId == null || receiverId == null || currencyId == null || transferAmount == null) {
            return ResponseEntity.badRequest().build();
        }

        savingsAccountsService.transferAmount(senderId, receiverId, currencyId, transferAmount, fee, requestId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
