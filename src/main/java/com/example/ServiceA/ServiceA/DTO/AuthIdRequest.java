package com.example.ServiceA.ServiceA.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthIdRequest (

    @Schema(
            description = "JWT token to validate",
            example = "eyJhbGciOiJIUzI1NiJ9..."
    )
    @NotBlank(
            message = "AuthId_JWT cannot be null or blank!"
    )
    @NotNull(
            message = "AuthId_JWT cannot be null!"
    )
    @JsonProperty(value = "AuthId")
    String authIdJWT
) {}
