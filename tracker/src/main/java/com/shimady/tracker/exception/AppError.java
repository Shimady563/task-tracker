package com.shimady.tracker.exception;

import java.time.LocalDateTime;

public record AppError(String message, String code, LocalDateTime time) {
}
