package org.example.orderserivce.service;


import org.example.orderserivce.entity.ProcessedOperation;
import org.example.orderserivce.exception.custom.AlreadyProcessedException;
import org.example.orderserivce.repository.ProcessedOperationsRepository;
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