package com.example.ServiceA.ServiceA.Service;

import com.example.ServiceA.ServiceA.Exception.InvalidAuthIdException;
import com.example.ServiceA.ServiceA.Utility.JwtUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AuthenticationIdentityService implements ValidateJWT {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @CircuitBreaker(name = "redisService", fallbackMethod = "AuthIdFallback")
    public ConcurrentHashMap<String, Object> createAuthIDJWT() {

        String sessionId = UUID.randomUUID() + "==";
        String authIdJWT = jwtUtil.generateAuthId_JWT(sessionId);

        String redisKey = "AuthId:" + authIdJWT;

        redisTemplate.opsForValue().set(
                redisKey,
                sessionId,
                Duration.ofMinutes(1)
        );

        ConcurrentHashMap<String, Object> result = new ConcurrentHashMap<>();
        result.put("authId", authIdJWT);
        result.put("timeStamp", Instant.now());

        return result;
    }

    public ConcurrentHashMap<String, Object> AuthIdFallback(Exception ex) {
        ConcurrentHashMap<String, Object> fallback = new ConcurrentHashMap<>();
        fallback.put("status", "failed");
        fallback.put("message", "Redis service is currently unavailable. Please try again later.");
        fallback.put("timeStamp", Instant.now());
        return fallback;
    }

    @CircuitBreaker(name = "redisService", fallbackMethod = "getAllAuthFallback")
    public List<ConcurrentHashMap<String, Object>> getAllAuthIDs() {

        Set<String> keys = redisTemplate.keys("AuthId:*");

        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }

        return keys.stream()
                .map(key -> {
                    Object authIdJWT = redisTemplate.opsForValue().get(key);

                    ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
                    map.put("redisKey", key);
                    map.put("sessionId", authIdJWT);

                    return map;
                })
                .collect(Collectors.toList());
    }

    public List<ConcurrentHashMap<String, Object>> getAllAuthFallback(Exception ex) {
        throw new RedisConnectionFailureException("Unable to connect to Redis. Please try again later.");
    }

    @CircuitBreaker(
            name = "redisService",
            fallbackMethod = "validateAuthFallback"
    )
    public Boolean validateAuthIDJWT(String authIdJWT) {

        try {

            String redisKey = "AuthId:" + authIdJWT;

            Object sessionIdInRedis =
                    redisTemplate.opsForValue().get(redisKey);

            if (sessionIdInRedis == null) {
                throw new InvalidAuthIdException(
                        "Invalid authId " + authIdJWT + " provided!"
                );
            }

            Map<String, Object> authIdData =
                    jwtUtil.decodePayload(authIdJWT);

            String sessionId =
                    (String) authIdData.get("sub");

            if (!sessionId.equals(sessionIdInRedis)) {
                throw new InvalidAuthIdException(
                        "AuthId JWT does not match session ID."
                );
            }

            return true;

        } catch (InvalidAuthIdException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    public Boolean validateAuthFallback(String AuthId_JWT, Exception ex) {
        throw new RedisConnectionFailureException("Unable to connect to Redis. Please try again later.");
    }


    public Boolean validateAuthIDJWTStatus(String AuthId_JWT, String expectedStatus) {

        if(!validateAuthIDJWT(AuthId_JWT))
        {
            throw new InvalidAuthIdException("Invalid or expired authId JWT. Please provide a valid authId JWT.");
        }
        Map<String, Object> authIdData = jwtUtil.decodePayload(AuthId_JWT);
        String status = (String) authIdData.get("status");
        if (!expectedStatus.equals(status)) {
            throw new InvalidAuthIdException("AuthId JWT is not valid for phone verification. Expected status: 'new', but found: '" + status + "'. Please provide a valid authId JWT with status 'new'.");
        }
        return true;
    }
}
