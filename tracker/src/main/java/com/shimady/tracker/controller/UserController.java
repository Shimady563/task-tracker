package com.shimady.tracker.controller;

import com.shimady.tracker.model.dto.UserDTO;
import com.shimady.tracker.model.dto.UserInfo;
import com.shimady.tracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Controller for creating, authenticating and updating users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/me")
    @Operation(summary = "Obtaining current user information")
    @SecurityRequirement(name = "SessionAuth")
    public UserInfo getCurrentUser() {
        return modelMapper.map(
                SecurityContextHolder
                        .getContext().
                        getAuthentication().
                        getPrincipal(),
                UserInfo.class
        );
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creating new user account")
    public void signUp(@Valid @RequestBody UserDTO request) {
        userService.createUser(request);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updating current user account")
    @SecurityRequirement(name = "SessionAuth")
    public void update(
            @PathVariable @Parameter(description = "Id of the user being updated", example = "1") Long id,
            @Valid @RequestBody UserDTO request
    ) {
        userService.updateUser(id, request);
    }

    // for documenting logout endpoint
    @PostMapping("/logout")
    @Operation(summary = "Logout current user")
    @SecurityRequirement(name = "SessionAuth")
    public void logout() {
    }

    // for documenting login endpoint
    // I use request params to , but I couldn't find a way to represent it in Swagger
    @PostMapping(value = "/login")
    @Operation(
            summary = "Login user",
            description = "Spring uses form-data for login by default, " +
                    "but I couldn't find a way to represent it in Swagger, " +
                    "you can find the right request format in the postman documentation")
    public void login(
            @RequestParam("username")
            @Parameter(description = "User email. " +
                    "I use email instead of username, because it's unique in my db schema",
                    example = "email@mail.com"
            )
            String email,

            @RequestParam("password")
            @Parameter(description = "User password", example = "StrongPassword123$")
            String password
    ) {
    }
}
