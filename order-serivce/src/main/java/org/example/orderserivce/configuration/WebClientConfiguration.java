package org.example.orderserivce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient marketWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:7073")
                .build();
    }

    @Bean
    public WebClient savingsWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:7072")
                .build();
    }
}
