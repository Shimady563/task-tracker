package com.shimady.tracker.repository;

import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.TaskStatus;
import com.shimady.tracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Task task1;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("Test User");
        user.setPassword("Password");
        user.setEmail("Email");
        user.setPhoneNumber("Phone Number");
        entityManager.persist(user);

        task1 = new Task();
        task1.setUser(user);
        task1.setStatus(TaskStatus.TODO);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setCreatedAt(LocalDateTime.now());
        task1.setDeadline(LocalDateTime.now());
        task1 = entityManager.persist(task1);

        Task task2 = new Task();
        task2.setUser(user);
        task2.setStatus(TaskStatus.TODO);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setCreatedAt(LocalDateTime.now());
        task2.setDeadline(LocalDateTime.now());
        entityManager.persist(task2);

        Task task3 = new Task();
        task3.setUser(user);
        task3.setStatus(TaskStatus.DONE);
        task3.setTitle("Task 3");
        task3.setDescription("Description 3");
        task3.setCreatedAt(LocalDateTime.now());
        task3.setDeadline(LocalDateTime.now());
        entityManager.persist(task3);

        entityManager.flush();
    }

    @Test
    public void shouldFindTaskById() {
        Optional<Task> foundTask = taskRepository.findById(task1.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo(task1.getTitle());
    }

    @Test
    public void shouldSaveTask() {
        Task newTask = new Task();
        newTask.setUser(user);
        newTask.setStatus(TaskStatus.TODO);
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");
        newTask.setCreatedAt(LocalDateTime.now());
        newTask.setDeadline(LocalDateTime.now());

        Task savedTask = taskRepository.save(newTask);

        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo(newTask.getTitle());
    }

    @Test
    public void shouldReturnFirstPageOfUserTasksWhenOffsetIs0() {
        Pageable pageable = PageRequest.of(0, 2);
        Slice<Task> tasks = taskRepository.findAllByUser(user, pageable);

        assertThat(tasks.getContent()).hasSize(2);
        assertThat(tasks.getContent().get(0).getUser()).isEqualTo(user);
        assertThat(tasks.hasNext()).isTrue();
    }

    @Test
    public void shouldReturnSecondPageOfUserTasksWhenOffsetIs1() {
        Pageable pageable = PageRequest.of(1, 2);
        Slice<Task> tasks = taskRepository.findAllByUser(user, pageable);

        assertThat(tasks.getContent()).hasSize(1);
        assertThat(tasks.getContent().get(0).getUser()).isEqualTo(user);
    }

    @Test
    public void shouldReturnEmptySliceOfTasksWhenUserHasNoTasks() {
        User newUser = new User();
        newUser.setUsername("New User");
        newUser.setPassword("Password");
        newUser.setEmail("Email 1");
        newUser.setPhoneNumber("Phone Number");
        newUser = userRepository.save(newUser);

        Pageable pageable = PageRequest.of(0, 2);
        Slice<Task> tasks = taskRepository.findAllByUser(newUser, pageable);
        assertThat(tasks.getContent()).isEmpty();
    }

    @Test
    public void shouldReturnNumberOfTasksForUserWithSpecifiedStatus() {
        long inProgressTaskCount = taskRepository.countByUserAndStatus(user, TaskStatus.TODO);
        assertThat(inProgressTaskCount).isEqualTo(2);
    }
}
