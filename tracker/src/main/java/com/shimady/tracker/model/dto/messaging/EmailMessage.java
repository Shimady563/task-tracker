package com.shimady.tracker.model.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailMessage {
    private String username;
    private String email;
}
