package com.shimady.notification.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PushMessage {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotNull(message = "Number of tasks to do cannot be null")
    private Long tasksToDo;
}
