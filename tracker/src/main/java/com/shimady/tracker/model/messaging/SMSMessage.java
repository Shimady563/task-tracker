package com.shimady.tracker.model.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class SMSMessage {
    private final String username;
    private final String phoneNumber;
}
