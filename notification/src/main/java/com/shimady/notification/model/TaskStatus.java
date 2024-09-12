package com.shimady.notification.model;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TaskStatus {
    TODO("To Do"),
    DEFERRED("Deferred"),
    DONE("Done");

    private String name;

    @Override
    public String toString() {
        return name;
    }
}
