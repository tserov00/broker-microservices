package org.example.marketservice.finnhub;

import jakarta.transaction.Transactional;
import org.example.marketservice.dto.response.FinnhubResponseDto;
import org.example.marketservice.entity.Security;
import org.example.marketservice.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FinnhubScheduler {

    @Value("${finnhub.api.key}")
    private String apiToken;

    private final RestTemplate restTemplate;
    private final SecurityRepository securityRepository;

    public FinnhubScheduler(RestTemplate restTemplate, SecurityRepository securityRepository) {
        this.restTemplate = restTemplate;
        this.securityRepository = securityRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void fetchPrice() {
        List<Security> securityList = securityRepository.findAll();

        try {
            for (Security security : securityList) {
                FinnhubResponseDto response = restTemplate.getForObject("https://finnhub.io/api/v1/quote?symbol=" + security.getTicker() + "&token=" + apiToken, FinnhubResponseDto.class);
                security.setLastPrice(response.getLastPrice());
            }
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println(e.getMessage());
        }

    }
}
