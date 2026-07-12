package com.example.ServiceA.ServiceA.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_kyc_table")
public class CustomerKYCTable {

    @Id
    private String kycId;
    private String documentType;
    private String documentNumber;
    private String documentImageUrl;
    private String verificationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerTable customer;
}
