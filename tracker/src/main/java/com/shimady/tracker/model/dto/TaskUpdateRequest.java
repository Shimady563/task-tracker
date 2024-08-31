package com.shimady.tracker.model.dto;

import com.shimady.tracker.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskUpdateRequest {

    @NotBlank(message = "title cannot be blank")
    public String title;

    @NotBlank(message = "description cannot be blank")
    public String description;

    @NotNull(message = "status cannot be null")
    public TaskStatus status;

    @NotNull(message = "deadline cannot be null")
    public LocalDateTime deadline;
}
