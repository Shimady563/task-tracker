package com.shimady.tracker.controller;

import com.shimady.tracker.model.dto.TaskCreationRequest;
import com.shimady.tracker.model.dto.TaskResponse;
import com.shimady.tracker.model.dto.TaskUpdateRequest;
import com.shimady.tracker.model.sort.TaskSort;
import com.shimady.tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@RequestBody TaskCreationRequest request) {
        taskService.createTask(request);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequest request) {
        taskService.updateTask(id, request);
    }

    @GetMapping("")
    public Slice<TaskResponse> getAllTasksForCurrentUser(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "id") TaskSort sort
    ) {
        return taskService.getAllByUser(limit, offset, sort);
    }

    /*
    @PutMapping("")

    @GetMapping() get all for current user
                  search by status
                  with pagination and sorting by creation time, deadline

    @DeleteMapping("")
     */
}
