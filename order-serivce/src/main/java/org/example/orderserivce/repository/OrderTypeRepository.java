package org.example.orderserivce.repository;


import org.example.orderserivce.entity.OrderType;
import org.example.orderserivce.enumeration.OrderTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderTypeRepository extends JpaRepository<OrderType, Integer> {
    Optional<OrderType> findByType(OrderTypeEnum type);
}
