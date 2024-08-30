package com.shimady.tracker.repository;

import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.TaskStatus;
import com.shimady.tracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1, user2;
    private Task task1, task2;

    @BeforeEach
    public void setup() {
        user1 = new User();
        user1.setUsername("User 1");
        user1.setPassword("Password 1");
        user1.setEmail("Email 1");
        user1.setPhoneNumber("Phone Number 1");

        user2 = new User();
        user2.setUsername("User 2");
        user2.setPassword("Password 2");
        user2.setEmail("Email 2");
        user2.setPhoneNumber("Phone Number 2");

        task1 = new Task();
        task1.setUser(user1);
        task1.setStatus(TaskStatus.TODO);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setCreatedAt(LocalDateTime.now());
        task1.setDeadline(LocalDateTime.now());

        task2 = new Task();
        task2.setUser(user2);
        task2.setStatus(TaskStatus.DEFERRED);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setCreatedAt(LocalDateTime.now());
        task2.setDeadline(LocalDateTime.now());

        user1 = entityManager.persist(user1);
        user2 = entityManager.persist(user2);
        task1 = entityManager.persist(task1);
        task2 = entityManager.persist(task2);

        entityManager.flush();
    }

    @Test
    public void shouldFindUserById() {
        Optional<User> foundUser = userRepository.findById(user1.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(user1.getUsername());
    }

    @Test
    public void shouldSaveUser() {
        User newUser = new User();
        newUser.setUsername("Bob Smith");
        newUser.setEmail("bob.smith@example.com");
        newUser.setPassword("Password");
        newUser.setPhoneNumber("Phone Number");

        User savedUser = userRepository.save(newUser);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(newUser.getEmail());
    }

    @Test
    public void shouldFindUserByEmail() {
        Optional<User> foundUser = userRepository.findByEmail(user1.getEmail());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
    public void shouldFindAllUsersAndFetchTasks() {
        List<User> users = userRepository.findAllFetchTasks();

        assertThat(users)
                .hasSize(2)
                .extracting(User::getEmail, User::getTasks)
                .containsExactly(
                        tuple(
                                user1.getEmail(),
                                user1.getTasks()),
                        tuple(
                                user2.getEmail(),
                                user2.getTasks()
                        )
                );
    }
}
