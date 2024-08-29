package com.shimady.tracker.model.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailMessage {
    private final String username;
    private final String email;
}
