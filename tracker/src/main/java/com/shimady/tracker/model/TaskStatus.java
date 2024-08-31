package com.shimady.tracker.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    TODO("To Do"),
    DEFERRED("Deferred"),
    DONE("Done");

    private final String name;
}
