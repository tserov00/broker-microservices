package org.example.savingsservice.repository;

import org.example.savingsservice.entity.TransactionType;
import org.example.savingsservice.enumeration.TransactionTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
    Optional<TransactionType> findByTransactionType(TransactionTypeEnum transactionType);
}
