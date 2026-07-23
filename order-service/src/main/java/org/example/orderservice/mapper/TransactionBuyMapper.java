package org.example.orderservice.mapper;

import org.example.orderservice.dto.request.SecurityDto;
import org.example.orderservice.dto.response.TransactionResponseDto;
import org.example.orderservice.entity.Transaction;
import org.example.orderservice.enumeration.OrderTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionBuyMapper {
    @Mapping(source = "security.ticker", target = "ticker")
    @Mapping(source = "transaction.buyOrder.orderType.type", target = "transactionType")
    @Mapping(source = "transaction.buyOrder.id", target = "orderId")
    TransactionResponseDto toResponseDto(Transaction transaction, SecurityDto security);

    default String map(OrderTypeEnum orderTypeEnum) {
        return orderTypeEnum.getDescription();
    }
}
