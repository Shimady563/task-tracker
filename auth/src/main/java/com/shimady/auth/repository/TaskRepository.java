package com.shimady.auth.repository;

import com.shimady.auth.model.Task;
import com.shimady.auth.model.TaskStatus;
import com.shimady.auth.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByUserAndStatus(User user, TaskStatus status);
}
