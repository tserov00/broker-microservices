package org.example.marketservice.service;


import org.example.marketservice.dto.response.MarketSecuritiesResponseDto;
import org.example.marketservice.entity.Security;
import org.example.marketservice.mapper.MarketMapper;
import org.example.marketservice.repository.SecurityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketService {

    private final SecurityRepository securityRepository;
    private final MarketMapper marketMapper;

    public MarketService(SecurityRepository securityRepository, MarketMapper marketMapper) {
        this.securityRepository = securityRepository;
        this.marketMapper = marketMapper;
    }

    @Transactional
    public List<MarketSecuritiesResponseDto> getSecurities() {
        List<Security> securityList = securityRepository.findAll();
        List<MarketSecuritiesResponseDto> marketSecuritiesResponseDtoList = new ArrayList<>();
        for (Security security : securityList) {
            marketSecuritiesResponseDtoList.add(marketMapper.toResponseDto(security));
        }
        return marketSecuritiesResponseDtoList;
    }

    @Transactional
    public List<MarketSecuritiesResponseDto> getSecuritiesBatchByIds(List<Long> ids) {

        List<Security> securities = securityRepository.findAllById(ids);

        return securities.stream()
                .map(marketMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public MarketSecuritiesResponseDto getSecurityById(Long id) {
        Security security = securityRepository.findById(id).orElse(null);
        return marketMapper.toResponseDto(security);
    }
}
