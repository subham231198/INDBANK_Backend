package com.example.ServiceA.ServiceA.Utility;

import com.example.ServiceA.ServiceA.Repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class GeneratorClass {

    @Autowired
    private CustomerRepo customerRepo;
    public String generateCustomerId() {
        long num = (long)(Math.random() * 1_000_000_000L);
        return "IN"+String.format("%09d", num);
    }

    public String getUniqueCustomerId() {
        String customerId;
        do {
            customerId = generateCustomerId();
        } while (customerRepo.existsByCustomerId(customerId));
        return customerId;
    }

    public String generateKYCId() {
        long num = (long)(Math.random() * 1_000_000_000L);
        return "KYC"+String.format("%09d", num);
    }


    public String generateAccountId() {
        long num = (long)(Math.random() * 1_000_000_000L);
        return "AC"+String.format("%09d", num);
    }

    public String encodeClientCredentials(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        return "Basic "+Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
