package com.shimady.tracker.exception.handler;

import com.shimady.tracker.exception.AccessDeniedException;
import com.shimady.tracker.exception.AppError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<AppError> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(
                new AppError(
                        e.getMessage(),
                        HttpStatus.FORBIDDEN.value(),
                        LocalDateTime.now()
                ),
                HttpStatus.FORBIDDEN
        );
    }
}
