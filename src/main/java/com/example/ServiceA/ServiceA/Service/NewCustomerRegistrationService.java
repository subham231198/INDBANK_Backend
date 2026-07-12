package com.example.ServiceA.ServiceA.Service;

import com.example.ServiceA.ServiceA.DTO.IncomingCustomerRegistrationRequest;
import com.example.ServiceA.ServiceA.Entity.CustomerAccountTable;
import com.example.ServiceA.ServiceA.Entity.CustomerAddressTable;
import com.example.ServiceA.ServiceA.Entity.CustomerKYCTable;
import com.example.ServiceA.ServiceA.Entity.CustomerOccupation;
import com.example.ServiceA.ServiceA.Entity.CustomerTable;
import com.example.ServiceA.ServiceA.Repository.CustomerRepo;
import com.example.ServiceA.ServiceA.Utility.ConfigReader;
import com.example.ServiceA.ServiceA.Utility.GeneratorClass;
import com.example.ServiceA.ServiceA.Utility.RestTemplateUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class NewCustomerRegistrationService {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private GeneratorClass generatorClass;

    @Autowired
    private RestTemplateUtility restTemplateUtility;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final Logger logger = LogManager.getLogger(NewCustomerRegistrationService.class);

    public boolean validateNewCustomerRegistration(
            String authIdJWT,
            String username,
            String email,
            String phone,
            String identityNumber
    ) {
        return verificationService.isCustomerIdUnique(authIdJWT, username)
                && verificationService.isEmailUnique(authIdJWT, email)
                && verificationService.isPhoneUnique(authIdJWT, phone)
                && verificationService.isIdentityNumberUnique(authIdJWT, identityNumber);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Map<String, Object>> registerNewCustomer(
            String authIdJWT,
            IncomingCustomerRegistrationRequest request
    ) {

        String customerId = generatorClass.getUniqueCustomerId();

        boolean isValid = validateNewCustomerRegistration(
                authIdJWT,
                customerId,
                request.customerRequest().email(),
                request.customerRequest().phoneNumber(),
                request.customerRequest().identityProofNumber()
        );

        if (!isValid) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Customer registration failed due to duplicate information."
                    ));
        }

        CustomerTable newCustomer = new CustomerTable();
        newCustomer.setCustomerId(customerId);
        newCustomer.setFirstName(request.customerRequest().firstName());
        newCustomer.setLastName(request.customerRequest().lastName());
        newCustomer.setEmail(request.customerRequest().email());
        newCustomer.setPhoneNumber(request.customerRequest().phoneNumber());
        newCustomer.setIdentityProofType(request.customerRequest().identityProofType());
        newCustomer.setIdentityProofNumber(request.customerRequest().identityProofNumber());
        newCustomer.setDateOfBirth(request.customerRequest().dateOfBirth());
        newCustomer.setGender(request.customerRequest().gender());
        newCustomer.setNationality(request.customerRequest().nationality());
        newCustomer.setOnboardingDate(Instant.now().toString());
        newCustomer.setStatus("PENDING");

        CustomerAddressTable address = new CustomerAddressTable();
        address.setAddress1(request.addressRequest().address1());
        address.setAddress2(request.addressRequest().address2());
        address.setCity(request.addressRequest().city());
        address.setState(request.addressRequest().state());
        address.setPostalCode(request.addressRequest().postalCode());
        address.setCustomer(newCustomer);

        CustomerKYCTable kyc = new CustomerKYCTable();
        kyc.setKycId(generatorClass.generateKYCId());
        kyc.setDocumentNumber("None");
        kyc.setDocumentType("None");
        kyc.setDocumentImageUrl("None");
        kyc.setVerificationStatus("Pending");
        kyc.setCustomer(newCustomer);

        CustomerOccupation occupation = new CustomerOccupation();
        occupation.setCompanyName(request.occupationRequest().companyName());
        occupation.setOccupation(request.occupationRequest().occupation());
        occupation.setAnnualIncome(request.occupationRequest().annualIncome());
        occupation.setJobTitle(request.occupationRequest().jobTitle());
        occupation.setYearsOfExperience(request.occupationRequest().yearsOfExperience());
        occupation.setCustomer(newCustomer);

        CustomerAccountTable account = new CustomerAccountTable();
        account.setAccountId(generatorClass.generateAccountId());
        account.setAccountNumber("TBD");
        account.setAccountType(request.accountRequest().accountType());
        account.setAccountStatus("Pending");
        account.setBalance(0.0);
        account.setIssuedDate("Pending");
        account.setLastUpdatedDate("Pending");
        account.setCustomer(newCustomer);

        newCustomer.setCustomerAddress(List.of(address));
        newCustomer.setCustomerOccupation(List.of(occupation));
        newCustomer.setCustomerKYC(List.of(kyc));
        newCustomer.setCustomerAccounts(List.of(account));

        boolean accountExists = validateAccountExists(
                request.accountRequest().accountType(),
                "NEW",
                "ACTIVE"
        );
        if(!accountExists){
            try {
                kafkaTemplate.send(
                        "account-creation-requests",
                        Map.of(
                                "accountType", request.accountRequest().accountType(),
                                "status", "NEW",
                                "state", "ACTIVE"
                        )
                ).get();
            }
            catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        try {

            customerRepo.saveAndFlush(newCustomer);

            logger.info(
                    "Publishing customer approval message = {}",
                    request
            );

            Map<String, Object> approvalPayload = Map.of(
                    "customerId", customerId,

                    "customer", Map.of(
                            "first_name", request.customerRequest().firstName(),
                            "last_name", request.customerRequest().lastName(),
                            "email", request.customerRequest().email(),
                            "contact", request.customerRequest().phoneNumber()
                    ),

                    "occupation", Map.of(
                            "annual_income",
                            request.occupationRequest().annualIncome()
                    ),
                    "location", request.addressRequest().city().toUpperCase(),

                    "account", Map.of(
                            "account_type",
                            request.accountRequest().accountType()
                    )
            );


            kafkaTemplate.send(
                    "new-customer-registrations-bom-approval",
                    approvalPayload
            ).get();

            logger.info("Message successfully sent for approval, {}", approvalPayload);

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Customer registration successful. Awaiting approval."
                    )
            );

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to publish customer registration event",
                    ex
            );
        }
    }

    public String retreiveAccessToken_ClientCredentials(){
        String url = ConfigReader.getConfigValue(
                "ServiceC.properties", "url1");


        String reqbody = "grant_type=client_credentials";
        Map<String, Object> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", generatorClass.encodeClientCredentials(
                ConfigReader.getConfigValue("ServiceC.properties", "client_id1"),
                ConfigReader.getConfigValue("ServiceC.properties", "client_secret1")
        ));

        logger.info("Requesting access token from Service C with client credentials grant. URL: {}, Headers: {}, Body: {}", url, headers, reqbody);

        Map<String, Object> response = restTemplateUtility.sendPostRequest(
                url,
                reqbody,
                headers
        );

        logger.info("Response from Service C for access token retrieval: {}", response);

        if(response.get("statusCode") != null && (Integer) response.get("statusCode") == 200){
            Map<String, Object> body = (Map<String, Object>) response.get("body");
            return body.get("access_token").toString();
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to retrieve access token: " + response);
        }
    }

    public Boolean validateAccountExists(String accountType, String status, String state){
        String url = ConfigReader.getConfigValue(
                "ServiceC.properties", "serviceC.account.verify.url");

        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("type", accountType.toUpperCase())
                .queryParam("status", status.toUpperCase())
                .queryParam("state", state.toUpperCase())
                .build()
                .toUri();

        String accessToken = retreiveAccessToken_ClientCredentials();

        Map<String, Object> headers = new LinkedHashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        logger.info("Validating account existence with Service C. URL: {}, Headers: {}", uri, headers);

        Map<String, Object> response = restTemplateUtility.sendPostRequest(
                uri.toString(),
                null,
                headers
        );

        logger.info("Response from Service C for account existence validation: {}", response);

        if(response.get("statusCode") != null && (Integer) response.get("statusCode") == 200){
            Map<String, Object> body = (Map<String, Object>) response.get("body");
            return (Boolean) body.get("isAccountExist");
        }
        else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to validate account existence: " + response);
        }
    }
}
