package com.example.ServiceA.ServiceA.Controller;


import com.example.ServiceA.ServiceA.DTO.AuthIdRequest;
import com.example.ServiceA.ServiceA.Service.AuthenticationIdentityService;
import com.example.ServiceA.ServiceA.Utility.ValidatorClass;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class AuthenticationIdentityController {

    @Autowired
    private AuthenticationIdentityService authenticationIdentityService;

    @Autowired
    private ValidatorClass validatorClass;

    @PostMapping(
            value = "/security/generateId",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Parameter(description = "Endpoint to generate authentication identity JWT")
    @ApiResponse(responseCode = "200", description = "Successfully generated authentication identity JWT")
    public ConcurrentHashMap<?, ?> generateAuthId(){
        return authenticationIdentityService.createAuthIDJWT();
    }

    @GetMapping(
            value = "/security/getAllAuthIds",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Parameter(description = "Endpoint to retrieve all authentication identity JWTs")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all authentication identity JWTs")
    public List<ConcurrentHashMap<String, Object>> getAllAuthIDs(){
        return authenticationIdentityService.getAllAuthIDs();
    }

    @PostMapping(
            value = "/security/validateAuthId",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request body containing the authentication identity JWT to validate",
            required = true
    )
    @Parameter(description = "Endpoint to validate the provided authentication identity JWT")
    public ResponseEntity<?> validateAuthIdJWT(@RequestBody AuthIdRequest request){
        validatorClass.authId_null_check(request.authIdJWT());
        Boolean isValid = authenticationIdentityService.validateAuthIDJWT(request.authIdJWT());
        if(isValid){
            return ResponseEntity.ok(Map.of("isJwtValid", true));
        }
        else {
            return ResponseEntity.ok(Map.of("isJwtValid", false));
        }
    }


}
