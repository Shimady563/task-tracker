package com.shimady.tracker.model.dto;

import com.shimady.tracker.model.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskResponse {
    private Long id;
    public String title;
    public String description;
    public TaskStatus status;
    public LocalDateTime createdAt;
    public LocalDateTime deadline;
}
