package com.example.ServiceA.ServiceA.Exception;

public class DuplicateCustomerEmailException extends RuntimeException {
    public DuplicateCustomerEmailException(String message) {
        super(message);
    }
}
