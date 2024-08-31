package com.shimady.tracker.service;

import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.dto.TaskCreationRequest;
import com.shimady.tracker.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SecurityContext securityContext;

    @Spy
    @InjectMocks
    private TaskService taskService;

    private MockedStatic<SecurityContextHolder> contextHolder;

    @BeforeEach
    public void setUp() {
        contextHolder = mockStatic(SecurityContextHolder.class);
        contextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    public void tearDown() {
        contextHolder.close();
    }

    @Test
    public void shouldCreateTask() {
        TaskCreationRequest request = new TaskCreationRequest();
        Task task = new Task();
        User user = new User();
        user.setTasks(List.of(task));

        given(securityContext.getAuthentication())
                .willReturn(new UsernamePasswordAuthenticationToken(user, null));
        given(modelMapper.map(eq(request), eq(Task.class))).willReturn(task);
        given(securityContext.getAuthentication())
                .willReturn(new UsernamePasswordAuthenticationToken(user, null));

        taskService.createTask(request);

        then(taskRepository).should().save(eq(task));

    }
}
