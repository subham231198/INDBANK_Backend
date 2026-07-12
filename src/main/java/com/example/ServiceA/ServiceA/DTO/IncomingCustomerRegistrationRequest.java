package com.example.ServiceA.ServiceA.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record IncomingCustomerRegistrationRequest(

        @JsonProperty("authentication")
        @NotNull(message = "authIdJwt required.")
        AuthIdRequest authIdRequest,

        @JsonProperty("customer")
        @NotNull(message = "customer details required.")
        IncomingCustomerRequest customerRequest,

        @JsonProperty("address")
        @NotNull(message = "address details required.")
        IncomingCustomerAddressRequest addressRequest,

        @JsonProperty("occupation")
        @NotNull(message = "occupation details required.")
        IncomingCustomerOccupationRequest occupationRequest,

        @JsonProperty("account")
        @NotNull(message = "account details required.")
        IncomingAccountRequest accountRequest
) {
}
