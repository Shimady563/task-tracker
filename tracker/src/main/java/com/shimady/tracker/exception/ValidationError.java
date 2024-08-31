package com.shimady.tracker.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ValidationError(List<Violation> violations, int statusCode, LocalDateTime time) {
    public record Violation(String field, String message) {
    }
}
