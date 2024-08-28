package com.shimady.tracker.service;

import com.shimady.tracker.exception.ResourceNotFoundException;
import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.dto.TaskCreationRequest;
import com.shimady.tracker.model.dto.TaskResponse;
import com.shimady.tracker.model.dto.TaskUpdateRequest;
import com.shimady.tracker.model.sort.TaskSort;
import com.shimady.tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void createTask(TaskCreationRequest request) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        log.info("Creating task for user with id {}", user.getId());

        Task task = modelMapper.map(request, Task.class);
        task.setUser(user);
        taskRepository.save(task);
    }

    @Transactional
    public void updateTask(Long id, TaskUpdateRequest request) {
        log.info("Updating task with id {}", id);

        Task task = getTaskById(id);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDeadline(request.getDeadline());
        task.setStatus(request.getStatus());

        taskRepository.save(task);
    }

    @Transactional
    public Slice<TaskResponse> getAllTasksByUser(int limit, int offset, TaskSort sort) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        log.info("Retrieving task for user with id {}", user.getId());

        Slice<Task> tasks = taskRepository.findAllByUser(
                user,
                PageRequest.of(offset, limit, sort.getSortValue())
        );

        return tasks.map((element) -> modelMapper.map(element, TaskResponse.class));
    }

    private Task getTaskById(Long id) {
        log.info("Retrieving task with id {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
    }

}
