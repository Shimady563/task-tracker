package com.shimady.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimady.tracker.config.TestSecurityConfig;
import com.shimady.tracker.model.TaskStatus;
import com.shimady.tracker.model.dto.TaskCreationRequest;
import com.shimady.tracker.model.dto.TaskUpdateRequest;
import com.shimady.tracker.model.sort.TaskSort;
import com.shimady.tracker.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import(TestSecurityConfig.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    @WithMockUser
    public void shouldCreateTask() throws Exception {
        TaskCreationRequest request = new TaskCreationRequest();
        request.setTitle("Title");
        request.setDescription("Description");
        request.setCreatedAt(LocalDateTime.now());
        request.setDeadline(LocalDateTime.now());

        mockMvc.perform(post("/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        then(taskService).should().createTask(any(TaskCreationRequest.class));
    }

    @Test
    @WithMockUser
    public void shouldUpdateTaskById() throws Exception {
        Long id = 1L;
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Title");
        request.setDescription("Description");
        request.setStatus(TaskStatus.DEFERRED);
        request.setDeadline(LocalDateTime.now());

        mockMvc.perform(put("/tasks/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        then(taskService).should().updateTask(eq(id), any(TaskUpdateRequest.class));
    }

    @Test
    @WithMockUser
    public void shouldFindAllTasksForCurrentUser() throws Exception {
        int limit = 5;
        int offset = 1;
        TaskSort sort = TaskSort.TITLE_ASC;

        mockMvc.perform(get("/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("limit", String.valueOf(limit))
                        .param("offset", String.valueOf(offset))
                        .param("sort", sort.toString()))
                .andExpect(status().isOk());

        then(taskService).should().getAllTasksByUser(eq(limit), eq(offset), eq(sort));
    }
}
