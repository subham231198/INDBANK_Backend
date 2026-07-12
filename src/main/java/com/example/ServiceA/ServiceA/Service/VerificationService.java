package com.example.ServiceA.ServiceA.Service;


import com.example.ServiceA.ServiceA.Repository.CustomerAccountRepo;
import com.example.ServiceA.ServiceA.Repository.CustomerRepo;
import com.example.ServiceA.ServiceA.Utility.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VerificationService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CustomerAccountRepo customerAccountRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationIdentityService authenticationIdentityService;

    private static final Logger logger = LogManager.getLogger(VerificationService.class);

    public boolean isCustomerIdUnique(String authIdJWT, String username) {
        logger.info("Validating uniqueness of customer ID: {}, JWT: {}", username, authIdJWT);
        authenticationIdentityService.validateAuthIDJWTStatus(authIdJWT, "new");
        if (username == null || username.isEmpty()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Username cannot be null or empty!"
            );
        }
        return !customerRepo.findByCustomerId(username).isPresent();
    }

    public boolean isEmailUnique(String authIdJWT, String email) {
        logger.info("Validating uniqueness of email: {}, JWT: {}", email, authIdJWT);
        authenticationIdentityService.validateAuthIDJWTStatus(authIdJWT, "new");
        if(email == null || email.isEmpty()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Email cannot be null or empty!"
            );
        }
        return !customerRepo.findByEmail(email).isPresent();
    }

    public boolean isPhoneUnique(String authIdJWT, String phone) {
        logger.info("Validating uniqueness of phone number: {}, JWT: {}", phone, authIdJWT);
        authenticationIdentityService.validateAuthIDJWTStatus(authIdJWT, "new");
        if (phone == null || phone.isEmpty()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Phone number cannot be null or empty!"
            );
        }
        return !customerRepo.findByPhoneNumber(phone).isPresent();
    }

    public boolean isIdentityNumberUnique(String authIdJWT, String identityNumber) {
        logger.info("Validating uniqueness of identity proof number: {}, JWT: {}", identityNumber, authIdJWT);
        authenticationIdentityService.validateAuthIDJWTStatus(authIdJWT, "new");
        if (identityNumber == null || identityNumber.isEmpty()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Identity proof number cannot be null or empty!"
            );
        }
        return !customerRepo.findByIdentityProofNumber(identityNumber).isPresent();
    }

    public boolean isAccountNumberUnique(String authIdJWT, String accountNumber) {
        logger.info("Validating uniqueness of account number: {}, JWT: {}", accountNumber, authIdJWT);
        authenticationIdentityService.validateAuthIDJWTStatus(authIdJWT, "new");
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Account number cannot be null or empty!"
            );
        }
        return !customerAccountRepo.findByAccountNumber(accountNumber).isPresent();
    }
}
