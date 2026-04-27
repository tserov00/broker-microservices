package org.example.authservice.repository;

import org.example.authservice.entity.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long> {
    public CustomerAccount findByLogin(String login);
}
