package com.example.ServiceA.ServiceA.Configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    private String maskSensitiveData(String body) {

        if (body == null || body.isEmpty()) {
            return body;
        }

        return body
                .replaceAll(
                        "(\"password\"\\s*:\\s*\")(.*?)(\")",
                        "$1****$3"
                )
                .replaceAll(
                        "(\"confirmPassword\"\\s*:\\s*\")(.*?)(\")",
                        "$1****$3"
                )
                .replaceAll(
                        "(\"token\"\\s*:\\s*\")(.*?)(\")",
                        "$1****$3"
                )
                .replaceAll(
                        "(\"ClientSecret\"\\s*:\\s*\")(.*?)(\")",
                        "$1****$3"
                );
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request, 1024 * 1024);

        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        long duration = System.currentTimeMillis() - startTime;

        String requestBody = maskSensitiveData(
                new String(
                        wrappedRequest.getContentAsByteArray(),
                        StandardCharsets.UTF_8
                )
        );

        String responseBody = new String(
                wrappedResponse.getContentAsByteArray(),
                StandardCharsets.UTF_8
        );

        logger.info("""
                
                ===== HTTP REQUEST =====
                Method      : {}
                URI         : {}
                Query Params: {}
                Request Body: {}
                
                ===== HTTP RESPONSE =====
                Status Code : {}
                Response    : {}
                Time Taken  : {} ms
                =========================
                """,
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                requestBody,
                response.getStatus(),
                responseBody,
                duration
        );

        wrappedResponse.copyBodyToResponse();
    }
}
