package com.shimady.tracker.repository;

import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Slice<Task> findAllByUser(User user, Pageable pageable);
}
