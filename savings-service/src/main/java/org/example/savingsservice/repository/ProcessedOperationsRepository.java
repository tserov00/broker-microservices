package org.example.savingsservice.repository;

import org.example.savingsservice.entity.ProcessedOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessedOperationsRepository extends JpaRepository<ProcessedOperation, UUID> {
}
