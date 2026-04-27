package org.example.orderserivce.repository;

import org.example.orderserivce.entity.TransactionSaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionSagaRepository extends JpaRepository<TransactionSaga, Long> {

    Optional<TransactionSaga> findTopBySagaIdOrderByCreatedAtDesc(UUID sagaId);

    List<TransactionSaga> findBySagaIdOrderByCreatedAtAsc(UUID sagaId);

    @Query("SELECT ts FROM TransactionSaga ts " +
            "WHERE ts.status = 'ACTIVE' " +
            "AND ts.createdAt < :before " +
            "AND ts.id = (SELECT MAX(ts2.id) FROM TransactionSaga ts2 WHERE ts2.sagaId = ts.sagaId)")
    List<TransactionSaga> findStaleActiveSagas(@Param("before") LocalDateTime before);
}
