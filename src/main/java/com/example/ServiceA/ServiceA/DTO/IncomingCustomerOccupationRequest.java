package com.example.ServiceA.ServiceA.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IncomingCustomerOccupationRequest(

        @JsonProperty("occupation")
        String occupation,

        @JsonProperty("company_name")
        String companyName,

        @JsonProperty("job_title")
        String jobTitle,

        @JsonProperty("years_of_experience")
        Integer yearsOfExperience,

        @JsonProperty("annual_income")
        Double annualIncome
) {
}
