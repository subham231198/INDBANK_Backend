package com.example.ServiceA.ServiceA.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IncomingAccountRequest(

        @JsonProperty("account_type")
        String accountType,

        @JsonProperty("product_code")
        String productCode,

        @JsonProperty("is_salary_account")
        Boolean isSalaryAccount,

        @JsonProperty("is_joint_account")
        Boolean isJointAccount

) {
}
