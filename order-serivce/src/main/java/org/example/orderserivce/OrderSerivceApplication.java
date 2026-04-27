package org.example.orderserivce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class OrderSerivceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderSerivceApplication.class, args);
    }

}
