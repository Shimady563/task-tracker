package com.shimady.tracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimady.tracker.exception.AppError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Http403ForbiddenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("An authentication error occurred: {}", authException.getMessage());
        if (authException.getAuthenticationRequest() != null) {
            log.debug("Authentication credentials: {}", authException.getAuthenticationRequest().getCredentials());
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        new AppError(
                                authException.getMessage(),
                                HttpStatus.FORBIDDEN.value()
                        )
                )
        );
    }
}
