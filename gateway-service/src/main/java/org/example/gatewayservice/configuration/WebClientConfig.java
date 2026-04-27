package org.example.gatewayservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient authWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:7071")
                .build();
    }

    @Bean
    public WebClient savingsWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:7072")
                .build();
    }

    @Bean
    public WebClient marketWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:7073")
                .build();
    }

    @Bean
    public WebClient orderWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:7074")
                .build();
    }

    @Bean
    public WebClient transactionWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:7074")
                .build();
    }

    @Bean
    public WebClient portfolioWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:7074")
                .build();
    }
}
