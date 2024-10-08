package com.shimady.notification.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SMSMessage {

    @NotBlank(message = "Email cannot be blank")
    private String username;

    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
}
