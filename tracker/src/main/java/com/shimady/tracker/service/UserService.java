package com.shimady.tracker.service;

import com.shimady.tracker.model.User;
import com.shimady.tracker.model.dto.UserDTO;
import com.shimady.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(UserDTO request) {
        log.info("Creating user with email {}", request.getEmail());
        User user = modelMapper.map(request, User.class);
        log.info(String.valueOf(user));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
