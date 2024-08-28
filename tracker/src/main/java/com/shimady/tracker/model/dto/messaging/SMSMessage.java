package com.shimady.tracker.model.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class SMSMessage {
    private String username;
    private String phoneNumber;
}
