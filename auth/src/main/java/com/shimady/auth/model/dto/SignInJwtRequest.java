package com.shimady.auth.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Login request payload to obtain JWT tokens")
public class SignInJwtRequest {
    @NotBlank(message = "email cannot be blank")
    @Schema(description = "User email", example = "student@example.com")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Schema(description = "User password", example = "P@ssw0rd!")
    private String password;
}
