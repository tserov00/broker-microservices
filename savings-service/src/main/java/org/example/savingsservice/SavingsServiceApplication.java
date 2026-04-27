package org.example.savingsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SavingsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SavingsServiceApplication.class, args);
    }

}
