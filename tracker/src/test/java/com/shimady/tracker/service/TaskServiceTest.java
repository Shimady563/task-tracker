package com.shimady.tracker.service;

import com.shimady.tracker.exception.AccessDeniedException;
import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.dto.TaskCreationRequest;
import com.shimady.tracker.model.dto.TaskUpdateRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void shouldUpdateTask() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        Task task = new Task();
        task.setId(1L);
        User user = new User();
        user.setId(1L);
        user.setTasks(List.of(task));
        task.setUser(user);

        given(securityContext.getAuthentication())
                .willReturn(new UsernamePasswordAuthenticationToken(user, null));
        given(taskRepository.findById(eq(task.getId()))).willReturn(Optional.of(task));

        taskService.updateTask(task.getId(), request);

        then(taskRepository).should().save(eq(task));
    }

    @Test
    public void shouldThrowAccessDeniedExceptionWhenIdsDoesNotMatchInUpdateTask() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        Task task = new Task();
        task.setId(1L);
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        user2.setTasks(List.of(task));
        task.setUser(user2);

        given(securityContext.getAuthentication())
                .willReturn(new UsernamePasswordAuthenticationToken(user1, null));
        given(taskRepository.findById(eq(task.getId()))).willReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.updateTask(task.getId(), request));
    }
}
