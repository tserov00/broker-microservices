package org.example.savingsservice.repository;

import org.example.savingsservice.entity.BalanceHistory;
import org.example.savingsservice.entity.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {
    List<BalanceHistory> findAllBySavingsAccountIn(List<SavingsAccount> savingsAccounts);
}
