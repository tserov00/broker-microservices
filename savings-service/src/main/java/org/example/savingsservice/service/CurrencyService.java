package org.example.savingsservice.service;

import org.example.savingsservice.dto.response.CurrencyResponseDto;
import org.example.savingsservice.entity.Currency;
import org.example.savingsservice.mapper.CurrencyMapper;
import org.example.savingsservice.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    public CurrencyResponseDto getCurrencyById(Long id) {
        Currency currency = currencyRepository.findById(id).orElse(null);
        return currencyMapper.toResponseDto(currency);
    }
}
