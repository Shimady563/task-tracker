package com.shimady.tracker.controller;

import com.shimady.tracker.model.dto.UserDTO;
import com.shimady.tracker.model.dto.UserInfo;
import com.shimady.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/me")
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
    public void signUp(@RequestBody UserDTO request) {
        userService.createUser(request);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody UserDTO request) {
        userService.updateUser(id, request);
    }
}
