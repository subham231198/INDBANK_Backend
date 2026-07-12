package com.example.ServiceA.ServiceA.Repository;

import com.example.ServiceA.ServiceA.Entity.CustomerOccupation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerOccupationRepo extends JpaRepository<CustomerOccupation, Long> {
    Optional<String> findOccupationByCustomerCustomerId(String customerId);
}
