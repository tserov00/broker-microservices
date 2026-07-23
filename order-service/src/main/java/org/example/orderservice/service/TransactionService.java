package org.example.orderservice.service;


import org.example.orderservice.client.SecurityClient;
import org.example.orderservice.dto.request.SecurityDto;
import org.example.orderservice.dto.response.TransactionResponseDto;
import org.example.orderservice.entity.Transaction;
import org.example.orderservice.mapper.TransactionBuyMapper;
import org.example.orderservice.mapper.TransactionSellMapper;
import org.example.orderservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionBuyMapper transactionBuyMapper;
    private final TransactionSellMapper transactionSellMapper;
    private final SecurityClient securityClient;

    public TransactionService(TransactionRepository transactionRepository, TransactionBuyMapper transactionBuyMapper, TransactionSellMapper transactionSellMapper, SecurityClient securityClient) {
        this.transactionRepository = transactionRepository;
        this.transactionBuyMapper = transactionBuyMapper;
        this.transactionSellMapper = transactionSellMapper;
        this.securityClient = securityClient;
    }

    public List<TransactionResponseDto> getAllTransactions(Long userId) {
        List<Transaction> buyTransactions =
                transactionRepository.findAllByBuyOrder_CustomerAccountId(userId);

        List<Transaction> sellTransactions =
                transactionRepository.findAllBySellOrder_CustomerAccountId(userId);

        Set<Long> securityIds = Stream.concat(
                buyTransactions.stream()
                        .map(t -> t.getBuyOrder().getSecurityId()),
                sellTransactions.stream()
                        .map(t -> t.getSellOrder().getSecurityId())
        ).collect(Collectors.toSet());

        List<SecurityDto> securities = securityClient.getByIds(new ArrayList<>(securityIds));

        Map<Long, SecurityDto> securityMap = securities.stream()
                .collect(Collectors.toMap(SecurityDto::getId, Function.identity()));

        List<TransactionResponseDto> responseList = new ArrayList<>();

        for (Transaction buyTransaction : buyTransactions) {
            Long securityId = buyTransaction.getBuyOrder().getSecurityId();
            SecurityDto security = securityMap.get(securityId);

            responseList.add(
                    transactionBuyMapper.toResponseDto(buyTransaction, security)
            );
        }

        for (Transaction sellTransaction : sellTransactions) {
            Long securityId = sellTransaction.getSellOrder().getSecurityId();
            SecurityDto security = securityMap.get(securityId);

            responseList.add(
                    transactionSellMapper.toResponseDto(sellTransaction, security)
            );
        }

        return responseList;
    }
}
