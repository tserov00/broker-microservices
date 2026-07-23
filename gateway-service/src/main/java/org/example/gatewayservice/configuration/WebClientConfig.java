package org.example.gatewayservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${auth.service.url}")
    private String authUrl;

    @Value("${savings.service.url}")
    private String savingsUrl;

    @Value("${market.service.url}")
    private String marketUrl;

    @Value("${order.service.url}")
    private String orderUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient authWebClient(WebClient.Builder builder) {
        return builder.baseUrl(authUrl).build();
    }

    @Bean
    public WebClient savingsWebClient(WebClient.Builder builder) {
        return builder.baseUrl(savingsUrl).build();
    }

    @Bean
    public WebClient marketWebClient(WebClient.Builder builder) {
        return builder.baseUrl(marketUrl).build();
    }

    @Bean
    public WebClient orderWebClient(WebClient.Builder builder) {
        return builder.baseUrl(orderUrl).build();
    }

    @Bean
    public WebClient transactionWebClient(WebClient.Builder builder) {
        return builder.baseUrl(orderUrl).build();
    }

    @Bean
    public WebClient portfolioWebClient(WebClient.Builder builder) {
        return builder.baseUrl(orderUrl).build();
    }
}