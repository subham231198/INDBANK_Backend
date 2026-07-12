package com.example.ServiceA.ServiceA.Repository;

import com.example.ServiceA.ServiceA.Entity.CustomerAddressTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepo extends JpaRepository<CustomerAddressTable, Long> {
    Optional<CustomerAddressTable> findByAddressId(Long addressId);
    List<CustomerAddressTable> findAddressByCustomerCustomerId(String customerId);
}
