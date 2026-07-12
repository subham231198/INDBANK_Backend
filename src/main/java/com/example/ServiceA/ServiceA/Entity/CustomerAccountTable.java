package com.example.ServiceA.ServiceA.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_account_table")
public class CustomerAccountTable {

    @Id
    @Column(unique = true, nullable = false)
    private String accountId;
    private String accountType;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Double balance;
    private String accountStatus;
    private String issuedDate;
    private String lastUpdatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerTable customer;
}
