package com.shimady.auth.exception.handler;

import com.shimady.auth.exception.AppError;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<AppError> handleJwtException(JwtException e) {
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new AppError(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
    }
}
