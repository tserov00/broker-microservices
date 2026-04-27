package org.example.orderserivce.repository;

import jakarta.persistence.LockModeType;
import org.example.orderserivce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findAllByCustomerAccountId(Long customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
SELECT o FROM Order o
WHERE o.orderType.type = org.example.orderserivce.enumeration.OrderTypeEnum.SELL
  AND o.orderStatus.status IN (
        org.example.orderserivce.enumeration.OrderStatusEnum.OPEN,
        org.example.orderserivce.enumeration.OrderStatusEnum.PARTIALLY_EXECUTED
  )
  AND o.securityId = :securityId
  AND o.price <= :price
ORDER BY o.createdAt ASC
""")
    List<Order> findBestSellMatchForBuy(
            @Param("securityId") Long securityId,
            @Param("price") BigDecimal price
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
SELECT o FROM Order o
WHERE o.orderType.type = org.example.orderserivce.enumeration.OrderTypeEnum.BUY
  AND o.orderStatus.status IN (
        org.example.orderserivce.enumeration.OrderStatusEnum.OPEN,
        org.example.orderserivce.enumeration.OrderStatusEnum.PARTIALLY_EXECUTED
  )
  AND o.securityId = :securityId
  AND o.price >= :price
ORDER BY o.createdAt ASC
""")
    List<Order> findBestBuyMatchForSell(
            @Param("securityId") Long securityId,
            @Param("price") BigDecimal price
    );
}
