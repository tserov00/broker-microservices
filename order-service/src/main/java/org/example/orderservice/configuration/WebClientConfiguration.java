package org.example.orderservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${market.service.url}")
    private String marketUrl;

    @Value("${savings.service.url}")
    private String savingsUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient marketWebClient(WebClient.Builder builder) {
        return builder.baseUrl(marketUrl).build();
    }

    @Bean
    public WebClient savingsWebClient(WebClient.Builder builder) {
        return builder.baseUrl(savingsUrl).build();
    }
}