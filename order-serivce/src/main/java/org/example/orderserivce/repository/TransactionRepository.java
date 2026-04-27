package org.example.orderserivce.repository;


import org.example.orderserivce.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = """
        SELECT t.*
        FROM transactions t
        LEFT JOIN orders o1 ON t.buy_order_id = o1.id
        LEFT JOIN orders o2 ON t.sell_order_id = o2.id
        WHERE o1.customer_account_id = :customerAccountId
           OR o2.customer_account_id = :customerAccountId
        """, nativeQuery = true)
    List<Transaction> findByCustomerAccount(@Param("customerAccountId") Long customerAccountId);

    List<Transaction> findAllByBuyOrder_CustomerAccountId(Long customerAccountId);
    List<Transaction> findAllBySellOrder_CustomerAccountId(Long customerAccountId);
}
