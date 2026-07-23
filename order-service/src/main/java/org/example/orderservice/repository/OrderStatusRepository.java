package org.example.orderservice.repository;

import org.example.orderservice.entity.OrderStatus;
import org.example.orderservice.enumeration.OrderStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    Optional<OrderStatus> findByStatus(OrderStatusEnum status);
}
