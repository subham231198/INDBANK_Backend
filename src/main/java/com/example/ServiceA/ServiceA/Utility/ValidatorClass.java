package com.example.ServiceA.ServiceA.Utility;

import com.example.ServiceA.ServiceA.Exception.DuplicateCustomerPANException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class ValidatorClass {

    public boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\d{10}$";
        return phoneNumber.matches(phoneRegex);
    }

    public boolean validateIdentityProofNumber(String identityProofType,
                                               String identityProofNumber) {

        if (identityProofType == null || identityProofNumber == null) {
            return false;
        }

        List<String> validProofTypes = List.of("AADHAR", "PAN", "PASSPORT", "DRIVING_LICENSE", "VOTER_ID");
        if (!validProofTypes.contains(identityProofType.toUpperCase())) {
            throw new DuplicateCustomerPANException.InvalidIdentityProofException("Invalid identity proof type: " + identityProofType);
        }

        return switch (identityProofType.toUpperCase()) {

            case "AADHAR" -> identityProofNumber.matches("^\\d{12}$");

            case "PAN" -> identityProofNumber.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");

            case "PASSPORT" -> identityProofNumber.matches("^[A-Z]{1}[0-9]{7}$");

            case "DRIVING_LICENSE" -> identityProofNumber.matches("^[A-Z]{2}[0-9]{2}\\s?[0-9]{11}$");

            case "VOTER_ID" -> identityProofNumber.matches("^[A-Z]{3}[0-9]{7}$");

            default -> false;
        };
    }

    public boolean validateDateOfBirth(String dateOfBirth) {
        String dobRegex = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$";
        return dateOfBirth != null && dateOfBirth.matches(dobRegex);
    }

    public boolean validateGender(String gender) {
        return gender != null && List.of("MALE", "FEMALE", "OTHER").contains(gender.toUpperCase());
    }

    public boolean validateNationality(String nationality) {
        return nationality != null && !nationality.trim().isEmpty() && nationality.equalsIgnoreCase("INDIAN");
    }

    public boolean validateOnboardingDate(String onboardingDate) {
        String dateRegex = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$";
        return onboardingDate != null && onboardingDate.matches(dateRegex);
    }

    public boolean validateCustomerId(String customerId) {
        String customerIdRegex = "^[A-Z]{2}\\d{10}$";
        return customerId != null && customerId.matches(customerIdRegex);
    }

    public boolean validatePostalCode(String postalCode) {
        String postalCodeRegex = "^\\d{6}$";
        return postalCode != null && postalCode.matches(postalCodeRegex);
    }

    public boolean validateNonEmptyString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public void authId_null_check(String authIdJWT) {
        if(authIdJWT == null || authIdJWT.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AuthId JWT cannot be null or blank!");
        }
    }
}
