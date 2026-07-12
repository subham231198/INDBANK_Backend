package com.example.ServiceA.ServiceA.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"code", "reason", "message"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public record APIResponse<T>(

        @JsonProperty("code")
        Integer code,

        @JsonProperty("reason")
        String reason,

        @JsonProperty("message")
        String message

) {}