package com.example.ServiceA.ServiceA.Repository;

import com.example.ServiceA.ServiceA.Entity.CustomerAccountTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAccountRepo extends JpaRepository<CustomerAccountTable, String> {
    Optional<CustomerAccountTable> findByAccountNumber(String accountNumber);
    List<CustomerAccountTable> findAccountByCustomerCustomerId(String customerId);
}
