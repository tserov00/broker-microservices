package org.example.orderservice.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaSenderService(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(String topic, String key, Object payload) {
        try {
            byte[] message = objectMapper.writeValueAsBytes(payload);
            kafkaTemplate.send(topic, key, message);
        } catch (Exception e) {
            throw new RuntimeException("Kafka send failed", e);
        }
    }

    public void send(String topic, UUID key, Object payload) {
        send(topic, key.toString(), payload);
    }
}
