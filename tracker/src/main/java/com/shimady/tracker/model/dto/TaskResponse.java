package com.shimady.tracker.model.dto;

import com.shimady.tracker.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Response entity for task information")
public class TaskResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "My Task")
    public String title;

    @Schema(example = "My Task description")
    public String description;

    @Schema(example = "TODO")
    public TaskStatus status;

    @Schema(example = "2024-08-24T22:06:43")
    public LocalDateTime createdAt;

    @Schema(example = "2024-08-25T22:06:43")
    public LocalDateTime deadline;
}
