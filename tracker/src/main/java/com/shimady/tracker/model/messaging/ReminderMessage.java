package com.shimady.tracker.model.messaging;

import com.shimady.tracker.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ReminderMessage {
    private final String username;
    private final String email;
    private final Map<TaskStatus, Integer> tasksByStatus;
}
