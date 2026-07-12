package com.example.ServiceA.ServiceA.Service;

@FunctionalInterface
public interface ValidateJWT {
    Boolean validateAuthIDJWTStatus(String AuthId_JWT, String expectedStatus);
}
