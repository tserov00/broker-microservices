package org.example.savingsservice.mapper;


import org.example.savingsservice.dto.response.SavingsAccountEntityResponseDto;
import org.example.savingsservice.dto.response.SavingsAccountResponseDto;
import org.example.savingsservice.entity.SavingsAccount;
import org.example.savingsservice.enumeration.CurrencyCodeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SavingsAccountMapper {
    @Mapping(source = "currencyId", target = "currencyCode")
    SavingsAccountResponseDto toResponseDto(SavingsAccount savingsAccount);

    SavingsAccountEntityResponseDto toEntityResponseDto(SavingsAccount savingsAccount);

    default String map(Long currencyId) {
        return CurrencyCodeEnum.fromId(currencyId).getCode();
    }
}
