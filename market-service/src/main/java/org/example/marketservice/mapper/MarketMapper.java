package org.example.marketservice.mapper;


import org.example.marketservice.dto.response.MarketSecuritiesResponseDto;
import org.example.marketservice.entity.Security;
import org.example.marketservice.enumeration.CurrencyCodeEnum;
import org.example.marketservice.enumeration.SecurityTypeEnum;
import org.example.marketservice.enumeration.StockExchangeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


    @Mapper(componentModel = "spring")
    public interface MarketMapper {
        @Mapping(source = "security.id", target = "id")
        @Mapping(source = "security.currencyId", target = "currencyCode")
        @Mapping(source = "securityType.securityType", target = "securityType")
        @Mapping(source = "stockExchange.stockExchangeName", target = "stockExchangeName")
        MarketSecuritiesResponseDto toResponseDto(Security security);

        default String map(SecurityTypeEnum securityTypeEnum) {
            return securityTypeEnum.getDescription();
        }

        default String map(Long currencyId) {
            return CurrencyCodeEnum.fromId(currencyId).getCode();
        }

        default String map(StockExchangeEnum stockExchangeEnum) {
            return stockExchangeEnum.getDescription();
        }
    }
