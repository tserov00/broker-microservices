package org.example.marketservice.repository;


import org.example.marketservice.entity.SecurityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityTypeRepository extends JpaRepository<SecurityType, Long> {
}
