package com.shimady.auth.model.dto;

import com.shimady.auth.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Sign up request payload to create a new user and receive JWT tokens")
public class SignUpJwtRequest {
    @NotBlank(message = "first name cannot be blank")
    @Schema(description = "User first name", example = "John")
    private String firstName;

    @NotBlank(message = "last name cannot be blank")
    @Schema(description = "User last name", example = "Doe")
    private String lastName;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "wrong format of email")
    @Schema(description = "User email", example = "student@example.com")
    private String email;

    @Password(message = "password should be at least 8 characters long " +
            "and contain at least one lower case letter, " +
            "upper case letter, " +
            "digit, " +
            "symbol from @#$%^&+=!?*")
    @Schema(description = "User password", example = "P@ssw0rd!")
    private String password;

    @NotNull(message = "group id cannot be null")
    @Schema(description = "Target group id for the user", example = "1")
    private Long groupId;
}
