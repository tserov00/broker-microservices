package org.example.savingsservice.repository;

import org.example.savingsservice.entity.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Integer> {
    Optional<SavingsAccount> findByCurrencyIdAndAccountId(Long currencyId, Long accountId);
    List<SavingsAccount> findAllByAccountId(Long customerAccountId);
}