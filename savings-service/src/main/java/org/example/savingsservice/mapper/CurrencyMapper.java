package org.example.savingsservice.mapper;

import org.example.savingsservice.dto.response.CurrencyResponseDto;
import org.example.savingsservice.entity.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyResponseDto toResponseDto(Currency currency);
}
