package com.example.ServiceA.ServiceA.Exception;

import com.example.ServiceA.ServiceA.DTO.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<APIResponse<Void>> handleResponseStatus(
            ResponseStatusException ex
    ) {

        String reason = switch (ex.getStatusCode().value()) {

            case 400 -> "Bad Request";

            case 401 -> "Unauthorized";

            case 403 -> "Forbidden";

            case 404 -> "Not Found";

            case 500 -> "Internal Server Error";

            default -> "Unexpected Error";
        };

        APIResponse<Void> response =
                new APIResponse<>(
                        ex.getStatusCode().value(),
                        reason,
                        ex.getReason() != null
                                ? ex.getReason()
                                : "Unexpected error"
                );

        return new ResponseEntity<>(
                response,
                ex.getStatusCode()
        );
    }

    @ExceptionHandler(InvalidAuthIdException.class)
    public ResponseEntity<?> invalidAuthId(){
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("status", "failed");
        result.put("timeStamp", Instant.now());
        result.put("reason", "authId jwt either expired or invalid");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<?> redisConnectionFailure(){
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("status", "failed");
        result.put("timeStamp", Instant.now());
        result.put("reason", "Unable to connect to Redis. Please try again later.");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),
                                error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
}
