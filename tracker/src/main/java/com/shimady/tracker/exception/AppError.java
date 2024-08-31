package com.shimady.tracker.exception;

import java.time.LocalDateTime;

public record AppError(String message, int code, LocalDateTime time) {
}
