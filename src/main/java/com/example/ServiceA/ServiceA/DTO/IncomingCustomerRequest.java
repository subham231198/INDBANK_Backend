package com.example.ServiceA.ServiceA.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record IncomingCustomerRequest(

        @JsonProperty("first_name")
        @NotNull
        String firstName,

        @JsonProperty("last_name")
        @NotNull
        String lastName,

        @JsonProperty("email")
        @NotNull
        String email,

        @JsonProperty("contact")
        @NotNull
        String phoneNumber,

        @JsonProperty("identity_proof_type")
        @NotNull
        String identityProofType,

        @JsonProperty("identity_proof_number")
        @NotNull
        String identityProofNumber,

        @JsonProperty("dob")
        @NotNull
        String dateOfBirth,

        @JsonProperty("gender")
        @NotNull
        String gender,

        @JsonProperty("nationality")
        @NotNull
        String nationality
)
{
}
