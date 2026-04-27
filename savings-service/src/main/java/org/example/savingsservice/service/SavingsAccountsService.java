package org.example.savingsservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.savingsservice.dto.request.SavingsAccountTransactionDto;
import org.example.savingsservice.dto.response.SavingsAccountEntityResponseDto;
import org.example.savingsservice.dto.response.SavingsAccountHistoryResponseDto;
import org.example.savingsservice.dto.response.SavingsAccountResponseDto;
import org.example.savingsservice.entity.BalanceHistory;
import org.example.savingsservice.entity.Currency;
import org.example.savingsservice.entity.SavingsAccount;
import org.example.savingsservice.entity.TransactionType;
import org.example.savingsservice.enumeration.CurrencyCodeEnum;
import org.example.savingsservice.enumeration.TransactionTypeEnum;
import org.example.savingsservice.exception.custom.InsufficientBalanceException;
import org.example.savingsservice.exception.custom.MissingCurrencyException;
import org.example.savingsservice.mapper.BalanceHistoryMapper;
import org.example.savingsservice.mapper.SavingsAccountMapper;
import org.example.savingsservice.repository.BalanceHistoryRepository;
import org.example.savingsservice.repository.CurrencyRepository;
import org.example.savingsservice.repository.SavingsAccountRepository;
import org.example.savingsservice.repository.TransactionTypeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class SavingsAccountsService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final SavingsAccountRepository savingsAccountRepository;
    private final CurrencyRepository currencyRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final SavingsAccountMapper savingsAccountMapper;
    private final BalanceHistoryMapper balanceHistoryMapper;
    private final IdempotencyService idempotencyService;

    public SavingsAccountsService(SavingsAccountRepository savingsAccountRepository, CurrencyRepository currencyRepository, BalanceHistoryRepository balanceHistoryRepository, TransactionTypeRepository transactionTypeRepository, SavingsAccountMapper savingsAccountMapper, BalanceHistoryMapper balanceHistoryMapper, IdempotencyService idempotencyService, EntityManager entityManager) {
        this.savingsAccountRepository = savingsAccountRepository;
        this.currencyRepository = currencyRepository;
        this.balanceHistoryRepository = balanceHistoryRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.savingsAccountMapper = savingsAccountMapper;
        this.balanceHistoryMapper = balanceHistoryMapper;
        this.idempotencyService = idempotencyService;
        this.entityManager = entityManager;
    }

    @Transactional
    public List<SavingsAccountResponseDto> getSavingsAccounts(Long userId) {
        List<SavingsAccount> savingsAccounts = savingsAccountRepository.findAllByAccountId(userId);
        List<SavingsAccountResponseDto> savingsAccountResponseDto = new ArrayList<>();
        for (SavingsAccount savingsAccount : savingsAccounts) {
            savingsAccountResponseDto.add(savingsAccountMapper.toResponseDto(savingsAccount));
        }
        return savingsAccountResponseDto;
    }

    @Transactional
    public List<SavingsAccountHistoryResponseDto> getHistory(Long userId) {
        List<SavingsAccount> savingsAccounts = savingsAccountRepository.findAllByAccountId(userId);
        List<BalanceHistory> balanceHistory = balanceHistoryRepository.findAllBySavingsAccountIn(savingsAccounts);
        List<SavingsAccountHistoryResponseDto> savingsAccountHistoryResponseDto = new ArrayList<>();
        for (BalanceHistory history : balanceHistory) {
            savingsAccountHistoryResponseDto.add(balanceHistoryMapper.toResponseDto(history));
        }
        return savingsAccountHistoryResponseDto;
    }

    @Transactional
    public void deposit(Long userId, SavingsAccountTransactionDto transactionDto) {
        CurrencyCodeEnum currencyEnum;

        try {
             currencyEnum = CurrencyCodeEnum.valueOf(transactionDto.getCurrencyCode().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный код валюты");
        }

        Currency currency = currencyRepository.findByCode(currencyEnum).orElseThrow(() -> new RuntimeException("Валюта не найдена в базе данных"));

        entityManager.createNativeQuery("CALL deposit_balance(:p_amount, :p_currency_id, :p_customer_account_id)")
                .setParameter("p_amount", transactionDto.getAmount())
                .setParameter("p_currency_id", currency.getId().intValue())
                .setParameter("p_customer_account_id", userId.intValue())
                .executeUpdate();
    }

    @Transactional
    public void withdraw(Long userId, SavingsAccountTransactionDto transactionDto) {
        CurrencyCodeEnum currencyEnum;

        try {
            currencyEnum = CurrencyCodeEnum.valueOf(transactionDto.getCurrencyCode().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный код валюты");
        }

        Currency currency = currencyRepository.findByCode(currencyEnum).orElseThrow(() -> new RuntimeException("Валюта не найдена в базе данных"));

        SavingsAccount savingsAccount = savingsAccountRepository.findByCurrencyIdAndAccountId(currency.getId(), userId)
                .orElseThrow(() -> new MissingCurrencyException("У вас нет счета с нужной валютой"));

        BigDecimal availableAmount = savingsAccount.getBalance().subtract(savingsAccount.getReservedAmount());

        if (availableAmount.compareTo(transactionDto.getAmount()) < 0) {
            throw new InsufficientBalanceException("Недостаточно средств");
        }

        entityManager.createNativeQuery("CALL withdraw_balance(:p_amount, :p_currency_id, :p_customer_account_id)")
                .setParameter("p_amount", transactionDto.getAmount())
                .setParameter("p_currency_id", currency.getId().intValue())
                .setParameter("p_customer_account_id", userId.intValue())
                .executeUpdate();
    }

    @Transactional
    public void transferAmount(Long senderId, Long receiverId, Long currencyId, BigDecimal transferAmount, BigDecimal fee, UUID requestId) {
        idempotencyService.checkAndMark(requestId);

        SavingsAccount senderSavingsAccount = savingsAccountRepository.findByCurrencyIdAndAccountId
                (currencyId, senderId).orElseThrow(RuntimeException::new);
        SavingsAccount receiverSavingsAccount = savingsAccountRepository.findByCurrencyIdAndAccountId(
                currencyId, receiverId).orElseThrow(RuntimeException::new);

        BigDecimal availableAmount = senderSavingsAccount.getBalance().subtract(senderSavingsAccount.getReservedAmount());

        if (availableAmount.compareTo(transferAmount.add(fee)) < 0) {
            throw new InsufficientBalanceException("Недостаточно средств");
        }

        senderSavingsAccount.setBalance(senderSavingsAccount.getBalance().subtract(transferAmount).subtract(fee));
        BalanceHistory senderBalanceHistory = new BalanceHistory(senderSavingsAccount, LocalDateTime.now(), transferAmount.add(fee),
                transactionTypeRepository.findByTransactionType(TransactionTypeEnum.SENT).orElseThrow(RuntimeException::new));
        savingsAccountRepository.save(senderSavingsAccount);
        balanceHistoryRepository.save(senderBalanceHistory);

        receiverSavingsAccount.setBalance(receiverSavingsAccount.getBalance().add(transferAmount.subtract(fee)));
        BalanceHistory receiverBalanceHistory = new BalanceHistory(receiverSavingsAccount, LocalDateTime.now(), transferAmount.subtract(fee),
                transactionTypeRepository.findByTransactionType(TransactionTypeEnum.RECEIVED).orElseThrow(RuntimeException::new));
        savingsAccountRepository.save(receiverSavingsAccount);
        balanceHistoryRepository.save(receiverBalanceHistory);
    }

    @Transactional
    public void reserveAmount(Long userId, Long currencyId, BigDecimal amount, UUID requestId) {
        idempotencyService.checkAndMark(requestId);

        SavingsAccount account = savingsAccountRepository.findByCurrencyIdAndAccountId(
                currencyId, userId).orElseThrow(RuntimeException::new);
        BigDecimal reservedAmount = account.getReservedAmount().add(amount);
        if (reservedAmount.compareTo(account.getBalance()) > 0) {
            throw new InsufficientBalanceException("Недостаточно средств");
        }
        account.setReservedAmount(reservedAmount);
        savingsAccountRepository.save(account);
    }

    @Transactional
    public void releaseAmount(Long userId, Long currencyId, BigDecimal amount, UUID requestId) {
        idempotencyService.checkAndMark(requestId);

        SavingsAccount account = savingsAccountRepository.findByCurrencyIdAndAccountId(
                currencyId, userId).orElseThrow(RuntimeException::new);
        BigDecimal reservedAmount = account.getReservedAmount().subtract(amount);
        if (account.getReservedAmount().compareTo(amount) < 0) {
            System.out.println(account.getReservedAmount());
            System.out.println(amount);
            throw new InsufficientBalanceException("Недостаточно средств");}
        account.setReservedAmount(reservedAmount);
        savingsAccountRepository.save(account);
    }

    @Transactional
    public SavingsAccountEntityResponseDto getSavingsAccount(Long currencyId, Long accountId) {
        SavingsAccount savingsAccount = savingsAccountRepository.findByCurrencyIdAndAccountId(currencyId, accountId).orElse(null);
        return savingsAccountMapper.toEntityResponseDto(savingsAccount);
    }
}
