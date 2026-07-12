package com.example.ServiceA.ServiceA.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IncomingCustomerAddressRequest(

        @JsonProperty("address1")
        String address1,

        @JsonProperty("address2")
        String address2,

        @JsonProperty("city")
        String city,

        @JsonProperty("state")
        String state,

        @JsonProperty("postalCode")
        String postalCode
) {
}
