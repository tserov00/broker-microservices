package org.example.orderserivce.repository;

import org.example.orderserivce.entity.CustomerPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerPortfolioRepository extends JpaRepository<CustomerPortfolio, Long> {
    List<CustomerPortfolio> findAllByCustomerAccountId(Long customerId);
    Optional<CustomerPortfolio> findByCustomerAccountIdAndSecurityId(Long customerAccountId, Long securityId);
}
