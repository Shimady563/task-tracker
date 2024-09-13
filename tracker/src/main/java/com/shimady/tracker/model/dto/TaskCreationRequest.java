package com.shimady.tracker.model.dto;

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
@Schema(description = "Request entity for task creation")
public class TaskCreationRequest {

    @NotBlank(message = "title cannot be blank")
    @Schema(example = "My Task")
    public String title;

    @NotBlank(message = "description cannot be blank")
    @Schema(example = "My Task Description")
    public String description;

    @NotNull(message = "creation date cannot be null")
    @Schema(example = "2024-08-24T22:06:43")
    public LocalDateTime createdAt;

    @NotNull(message = "deadline cannot be null")
    @Schema(example = "2024-08-25T22:06:43")
    public LocalDateTime deadline;
}
