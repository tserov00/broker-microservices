package org.example.savingsservice.service;

import org.example.savingsservice.entity.ProcessedOperation;
import org.example.savingsservice.exception.custom.AlreadyProcessedException;
import org.example.savingsservice.repository.ProcessedOperationsRepository;
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