package com.shimady.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@Schema(description = "Authenticated user details")
public class UserResponse {

    @Schema(description = "User id", example = "10")
    private Long id;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Email", example = "student@example.com")
    private String email;

    @Schema(description = "Group name (Teacher if no group)", example = "CS101")
    private String groupName;
    
    @Schema(description = "Group id (-1 if teacher)", example = "1")
    private Long groupId;
}
