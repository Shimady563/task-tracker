package com.shimady.tracker.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskCreationRequest {
    public String title;
    public String description;
    public LocalDateTime createdAt;
    public LocalDateTime deadline;
}
