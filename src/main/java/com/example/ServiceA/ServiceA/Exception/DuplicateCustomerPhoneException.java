package com.example.ServiceA.ServiceA.Exception;

public class DuplicateCustomerPhoneException extends RuntimeException {
    public DuplicateCustomerPhoneException(String message) {
        super(message);
    }
}
