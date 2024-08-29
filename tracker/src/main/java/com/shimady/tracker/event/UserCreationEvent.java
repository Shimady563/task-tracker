package com.shimady.tracker.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreationEvent {
    private Long userId;
}
