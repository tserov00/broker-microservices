package org.example.orderservice.mapper;

import org.example.orderservice.dto.request.SecurityDto;
import org.example.orderservice.dto.response.OrderResponseDto;
import org.example.orderservice.entity.Order;
import org.example.orderservice.enumeration.OrderStatusEnum;
import org.example.orderservice.enumeration.OrderTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "security.ticker", target = "ticker")
    @Mapping(source = "order.orderType.type", target = "orderType")
    @Mapping(source = "security.lastPrice", target = "currentPrice")
    @Mapping(source = "order.orderStatus.status", target = "orderStatus")
    @Mapping(source = "order.availableQuantity", target = "availableQuantity", defaultExpression = "java(0)")
    @Mapping(source = "order.id", target = "id")
    OrderResponseDto toOrderResponseDto(Order order, SecurityDto security);

    default String map(OrderTypeEnum orderTypeEnum) {
        return orderTypeEnum.getDescription();
    }

    default String map(OrderStatusEnum orderStatusEnum) {
        return orderStatusEnum.getDescription();
    }
}
