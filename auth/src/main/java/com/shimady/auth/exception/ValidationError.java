package com.shimady.auth.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Validation error details")
public record ValidationError(

        @Schema(description = "List of violations")
        List<Violation> violations,

        @Schema(description = "HTTP status code", example = "400")
        int statusCode
) {
    public record Violation(String field, String message) {
    }
}

