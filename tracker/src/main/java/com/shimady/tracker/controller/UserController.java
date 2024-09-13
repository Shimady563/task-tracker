package com.shimady.tracker.controller;

import com.shimady.tracker.model.dto.UserDTO;
import com.shimady.tracker.model.dto.UserInfo;
import com.shimady.tracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    public void update(
            @PathVariable @Parameter(description = "Id of the user being updated") Long id,
            @Valid @RequestBody UserDTO request
    ) {
        userService.updateUser(id, request);
    }
}
