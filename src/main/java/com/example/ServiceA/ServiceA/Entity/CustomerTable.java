package com.example.ServiceA.ServiceA.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_table")
public class CustomerTable {
    @Id
    @Column(name = "customerId", nullable = false, unique = true)
    private String customerId;
    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    private String identityProofType;

    @Column(unique = true, nullable = false)
    private String identityProofNumber;

    @Column(nullable = false)
    private String dateOfBirth;
    private String gender;
    private String nationality;
    private String onboardingDate;
    private String status;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerAddressTable> customerAddress;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerOccupation> customerOccupation;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerKYCTable> customerKYC;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerAccountTable> customerAccounts;


    @Override
    public String toString() {
        return "CustomerTable{" +
                "customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", identityProofType='" + identityProofType + '\'' +
                ", identityProofNumber='" + identityProofNumber + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
                ", onboardingDate='" + onboardingDate + '\'' +
                ", status='" + status + '\'' +
                ", customerAddressCount=" + (customerAddress != null ? customerAddress.size() : 0) +
                ", customerOccupationCount=" + (customerOccupation != null ? customerOccupation.size() : 0) +
                ", customerKYCCount=" + (customerKYC != null ? customerKYC.size() : 0) +
                ", customerAccountsCount=" + (customerAccounts != null ? customerAccounts.size() : 0) +
                '}';
    }
}