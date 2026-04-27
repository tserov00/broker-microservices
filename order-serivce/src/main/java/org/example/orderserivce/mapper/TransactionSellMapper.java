package org.example.orderserivce.mapper;


import org.example.orderserivce.dto.request.SecurityDto;
import org.example.orderserivce.dto.response.TransactionResponseDto;
import org.example.orderserivce.entity.Transaction;
import org.example.orderserivce.enumeration.OrderTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionSellMapper {
    @Mapping(source = "security.ticker", target = "ticker")
    @Mapping(source = "transaction.sellOrder.orderType.type", target = "transactionType")
    @Mapping(source = "transaction.sellOrder.id", target = "orderId")
    TransactionResponseDto toResponseDto(Transaction transaction, SecurityDto security);

    default String map(OrderTypeEnum orderTypeEnum) {
        return orderTypeEnum.getDescription();
    }
}
