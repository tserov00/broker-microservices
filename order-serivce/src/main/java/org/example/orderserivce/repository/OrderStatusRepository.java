package org.example.orderserivce.repository;

import org.example.orderserivce.entity.OrderStatus;
import org.example.orderserivce.enumeration.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    Optional<OrderStatus> findByStatus(OrderStatusEnum status);
}
