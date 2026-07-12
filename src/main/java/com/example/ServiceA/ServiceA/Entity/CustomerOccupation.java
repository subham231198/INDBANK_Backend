package com.example.ServiceA.ServiceA.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_occupation_table")
public class CustomerOccupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long occupationId;
    private String occupation;
    private String companyName;
    private String jobTitle;
    private Integer yearsOfExperience;
    private Double annualIncome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerTable customer;
}
