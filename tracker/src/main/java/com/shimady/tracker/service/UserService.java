package com.shimady.tracker.service;

import com.shimady.tracker.event.UserCreationEvent;
import com.shimady.tracker.exception.AccessDeniedException;
import com.shimady.tracker.exception.ResourceNotFoundException;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.dto.UserDTO;
import com.shimady.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createUser(UserDTO request) {
        log.info("Creating user with email {}", request.getEmail());
        User user = modelMapper.map(request, User.class);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        eventPublisher.publishEvent(new UserCreationEvent(user.getId()));
    }

    @Transactional
    public void updateUser(Long id, UserDTO request) {
        log.info("Updating user with email {}", request.getEmail());

        SecurityContext context = SecurityContextHolder.getContext();
        User user = (User) context.getAuthentication().getPrincipal();

        if (!user.getId().equals(id)) {
            throw new AccessDeniedException("Access denied for user update request, user id " + id);
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        context.setAuthentication(newAuthentication);

        userRepository.save(user);
    }
}
