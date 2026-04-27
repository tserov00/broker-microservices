package org.example.savingsservice.mapper;

import org.example.savingsservice.dto.response.SavingsAccountHistoryResponseDto;
import org.example.savingsservice.entity.BalanceHistory;
import org.example.savingsservice.enumeration.CurrencyCodeEnum;
import org.example.savingsservice.enumeration.TransactionTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceHistoryMapper {
    @Mapping(source = "savingsAccount.currencyId", target = "currencyCode")
    @Mapping(source = "transactionType.transactionType", target = "transactionType")
    SavingsAccountHistoryResponseDto toResponseDto(BalanceHistory balanceHistory);

    default String map(Long currencyId) {
        return CurrencyCodeEnum.fromId(currencyId).getCode();
    }

    default String map(TransactionTypeEnum transactionTypeEnum) {
        return transactionTypeEnum.getDescription();
    }
}
