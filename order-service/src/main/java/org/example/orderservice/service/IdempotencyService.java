package org.example.orderservice.service;


import org.example.orderservice.entity.ProcessedOperation;
import org.example.orderservice.exception.custom.AlreadyProcessedException;
import org.example.orderservice.repository.ProcessedOperationsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdempotencyService {

    private final ProcessedOperationsRepository repo;

    public IdempotencyService(ProcessedOperationsRepository repo) {
        this.repo = repo;
    }

    public void checkAndMark(UUID operationId) {

        if (repo.existsById(operationId)) {
            throw new AlreadyProcessedException("Operation already processed");
        }

        repo.save(new ProcessedOperation(operationId));
    }
}