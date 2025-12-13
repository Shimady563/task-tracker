package com.shimady.auth.service;

import com.shimady.auth.converter.AuthConverter;
import com.shimady.auth.model.Group;
import com.shimady.auth.model.User;
import com.shimady.auth.model.dto.JwtResponse;
import com.shimady.auth.model.dto.SignInJwtRequest;
import com.shimady.auth.model.dto.SignUpJwtRequest;
import com.shimady.auth.model.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final GroupService groupService;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        User user = userService.getUserByEmail(getUserEmail());
        return AuthConverter.domain2Response(user);
    }

    @Transactional
    public JwtResponse signUp(SignUpJwtRequest request) {
        log.info("Signing up user with email: {}", request.getEmail());
        User user = AuthConverter.signUpRequest2Domain(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Group group = groupService.getGroupById(request.getGroupId());
        group.addUser(user);
        userService.saveUser(user);
        return jwtService.generateTokens(user);
    }

    @Transactional(readOnly = true)
    public JwtResponse authenticate(SignInJwtRequest request) {
        log.info("Authenticating user with email: {}", request.getEmail());
        User user = userService.getUserByEmail(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password for user: " + user.getEmail());
        }
        return jwtService.generateTokens(user);
    }

    @Transactional
    public JwtResponse refreshToken(String token) {
        return jwtService.refreshToken(token);
    }

    @Transactional
    public void logout() {
        String email = getUserEmail();
        log.info("Logging out user with email: {}", email);
        jwtService.deleteTokenByEmail(email);
    }

    protected String getUserEmail() {
        return (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
