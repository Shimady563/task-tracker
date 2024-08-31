package com.shimady.tracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskCreationRequest {

    @NotBlank(message = "title cannot be blank")
    public String title;

    @NotBlank(message = "description cannot be blank")
    public String description;

    @NotNull(message = "creation date cannot be null")
    public LocalDateTime createdAt;

    @NotNull(message = "deadline cannot be null")
    public LocalDateTime deadline;
}
