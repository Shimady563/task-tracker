package com.shimady.tracker.model.dto;

import com.shimady.tracker.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "My Task")
    public String title;

    @NotBlank(message = "description cannot be blank")
    @Schema(example = "My Task description")
    public String description;

    @NotNull(message = "status cannot be null")
    public TaskStatus status;

    @NotNull(message = "deadline cannot be null")
    @Schema(example = "2024-08-25T22:06:43")
    public LocalDateTime deadline;
}
