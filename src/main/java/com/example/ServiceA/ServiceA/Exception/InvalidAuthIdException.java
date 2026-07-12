package com.example.ServiceA.ServiceA.Exception;

public class InvalidAuthIdException extends RuntimeException {
    public InvalidAuthIdException(String message) {
        super(message);
    }
}
