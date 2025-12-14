package com.shimady.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Authenticated user details")
public class UserResponse {

    @Schema(description = "User id", example = "10")
    private Long id;

    @Schema(description = "Username", example = "John")
    private String username;

    @Schema(description = "Phone number", example = "+76969696969")
    private String phoneNumber;

    @Schema(description = "Email", example = "student@example.com")
    private String email;
}
