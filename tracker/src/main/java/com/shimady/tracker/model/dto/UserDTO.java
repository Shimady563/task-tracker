package com.shimady.tracker.model.dto;

import com.shimady.tracker.validation.Password;
import com.shimady.tracker.validation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    @NotBlank(message = "username cannot be blank")
    @Size(min = 4, max = 64,
            message = "username length should be between 4 and 64 characters")
    @Schema(example = "My Username")
    private String username;

    @Password(message = "password should be at least 8 characters long " +
            "and contain at least one lower case letter, " +
            "upper case letter, digit, symbol from @#$%^&+= " +
            "and not contain any other characters")
    @Schema(example = "StrongPassword123$")
    private String password;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "wrong format of email")
    @Schema(example = "email@mail.com")
    private String email;

    @PhoneNumber(message = "phone number should start with " +
            "+7 or 8 and then contain 10 digits")
    @Schema(example = "+76969696969")
    private String phoneNumber;
}


