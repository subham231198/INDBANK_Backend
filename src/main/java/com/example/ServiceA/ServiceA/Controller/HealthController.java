package com.example.ServiceA.ServiceA.Controller;


import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class HealthController
{

    @GetMapping(
            value = "/health",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    @Parameter(description = "Endpoint to check the health status of the service")
    public ConcurrentHashMap<String, Object> health() {
        ConcurrentHashMap<String, Object> healthStatus = new ConcurrentHashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("service", "ServiceA");
        healthStatus.put("version", "1.0.0");
        healthStatus.put("timestamp", System.currentTimeMillis());
        return healthStatus;
    }
}
