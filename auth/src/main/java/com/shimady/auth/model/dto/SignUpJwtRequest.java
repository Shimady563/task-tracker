package com.shimady.auth.model.dto;

import com.shimady.auth.validation.Password;
import com.shimady.auth.validation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Sign up request payload to create a new user and receive JWT tokens")
public class SignUpJwtRequest {
    @NotBlank(message = "username cannot be blank")
    @Schema(description = "Username", example = "John")
    private String username;

    @PhoneNumber(message = "phone number should start with " +
            "+7 or 8 and then contain 10 digits")
    @Schema(example = "+76969696969")
    private String phoneNumber;

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
}
