package com.shimady.tracker.model.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PushMessage {
    private final String username;
    private final long tasksToDo;
}
