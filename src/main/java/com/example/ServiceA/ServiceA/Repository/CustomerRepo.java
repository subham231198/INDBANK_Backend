package com.example.ServiceA.ServiceA.Repository;

import com.example.ServiceA.ServiceA.Entity.CustomerTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerRepo extends JpaRepository<CustomerTable, String> {
    Optional<CustomerTable> findByIdentityProofNumber(String identityProofNumber);
    Optional<CustomerTable> findByEmail(String email);
    Optional<CustomerTable> findByPhoneNumber(String phoneNumber);
    Optional<CustomerTable> findByCustomerId(String customerId);
    Boolean existsByCustomerId(String customerId);
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByIdentityProofNumber(String identityProofNumber);
}
