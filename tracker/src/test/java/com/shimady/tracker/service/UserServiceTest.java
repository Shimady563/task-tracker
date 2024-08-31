package com.shimady.tracker.service;

import com.shimady.tracker.event.UserCreationEvent;
import com.shimady.tracker.exception.AccessDeniedException;
import com.shimady.tracker.model.Task;
import com.shimady.tracker.model.User;
import com.shimady.tracker.model.dto.UserDTO;
import com.shimady.tracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mockStatic;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    private MockedStatic<SecurityContextHolder> contextHolder;

    @BeforeEach
    public void setUp() {
        contextHolder = mockStatic(SecurityContextHolder.class);
        contextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    public void tearDown() {
        contextHolder.close();
    }

    @Test
    public void shouldCreateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("Password");

        User user = new User();
        user.setPassword(userDTO.getPassword());

        given(modelMapper.map(userDTO, User.class)).willReturn(user);
        given(passwordEncoder.encode(eq(user.getPassword()))).willReturn(userDTO.getPassword());

        userService.createUser(userDTO);

        then(userRepository).should().save(eq(user));
        then(eventPublisher).should().publishEvent(any(UserCreationEvent.class));

    }

    @Test
    public void shouldUpdateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Username");
        userDTO.setEmail("Email");
        userDTO.setPassword("Password");

        User user = new User();
        user.setId(1L);
        user.setUsername("Old username");
        user.setEmail("Old email");
        user.setPassword(userDTO.getPassword());

        given(securityContext.getAuthentication())
                .willReturn(new UsernamePasswordAuthenticationToken(user, null));
        given(passwordEncoder.encode(eq(user.getPassword()))).willReturn(userDTO.getPassword());

        userService.updateUser(user.getId(), userDTO);

        then(userRepository).should().save(eq(user));
    }

    @Test
    public void shouldThrowDenyAccessWhenUserIdsDoesNotMatch() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        user.setId(1L);

        given(securityContext.getAuthentication())
                .willReturn(new UsernamePasswordAuthenticationToken(user, null));

        assertThrows(AccessDeniedException.class,
                () -> userService.updateUser(2L, userDTO));
    }
}
