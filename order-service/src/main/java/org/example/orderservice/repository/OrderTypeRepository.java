package org.example.orderservice.repository;


import org.example.orderservice.entity.OrderType;
import org.example.orderservice.enumeration.OrderTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderTypeRepository extends JpaRepository<OrderType, Integer> {
    Optional<OrderType> findByType(OrderTypeEnum type);
}
