package com.shimady.tracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(generator = "task_id_seq")
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    public String title;

    public String description;

    @Enumerated(EnumType.STRING)
    public TaskStatus status = TaskStatus.TODO;

    public LocalDateTime createdAt;

    public LocalDateTime deadline;
}
