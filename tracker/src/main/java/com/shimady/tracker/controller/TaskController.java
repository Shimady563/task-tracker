package com.shimady.tracker.controller;

import com.shimady.tracker.model.dto.TaskCreationRequest;
import com.shimady.tracker.model.dto.TaskResponse;
import com.shimady.tracker.model.dto.TaskUpdateRequest;
import com.shimady.tracker.model.sort.TaskSort;
import com.shimady.tracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = "Controller for manipulating user tasks")
@SecurityRequirement(name = "SessionAuth")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating new task for current user")
    public void createTask(@Valid @RequestBody TaskCreationRequest request) {
        taskService.createTask(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update current user task by id")
    public void updateTask(
            @PathVariable @Parameter(description = "Id of the task being updated", example = "1") Long id,
            @Valid @RequestBody TaskUpdateRequest request
    ) {
        taskService.updateTask(id, request);
    }

    @GetMapping("")
    @Operation(summary = "Obtaining all tasks for current user")
    public Slice<TaskResponse> getAllTasksForCurrentUser(
            @RequestParam(defaultValue = "10")
            @Parameter(description = "Number of tasks per page")
            int limit,

            @RequestParam(defaultValue = "0")
            @Parameter(description = "Number of the page")
            int offset,

            @RequestParam(defaultValue = "ID_ASC")
            @Parameter(description = "Sort field and direction")
            TaskSort sort
    ) {
        return taskService.getAllTasksByUser(limit, offset, sort);
    }
}
