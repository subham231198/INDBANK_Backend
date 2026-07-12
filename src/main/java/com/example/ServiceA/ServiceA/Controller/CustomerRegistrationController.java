package com.example.ServiceA.ServiceA.Controller;


import com.example.ServiceA.ServiceA.DTO.IncomingCustomerRegistrationRequest;
import com.example.ServiceA.ServiceA.Service.NewCustomerRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CustomerRegistrationController {

    @Autowired
    private NewCustomerRegistrationService newCustomerRegistrationService;


    @PostMapping(
            value = "/v1/customers/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
//            headers = {
//                    "Authorization=Bearer {authIdJWT}",
//                    "Content-Type=application/json",
//            }
    )
    public ResponseEntity<?> registerCustomer(
            @Valid @RequestBody IncomingCustomerRegistrationRequest request,
            @RequestHeader("X-Channel") String channel,
            @RequestHeader(value = "Authorization") String authIdJWT
    ) {

        if(channel == null || channel.isEmpty()) {
            return ResponseEntity.badRequest().body("X-Channel header is missing or empty.");
        }
        if (authIdJWT == null || authIdJWT.isEmpty() || !authIdJWT.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Authorization header is missing, empty, or does not start with 'Bearer '."));
        }
        if(!channel.equalsIgnoreCase("mobile") && !channel.equalsIgnoreCase("web") && !channel.equalsIgnoreCase("branch")){
            return ResponseEntity.badRequest().body("Invalid X-Channel header value provided!");
        }

        String authIdToken = authIdJWT.substring(7);

        return newCustomerRegistrationService.registerNewCustomer(
                authIdToken,
                request
        );
    }

}
