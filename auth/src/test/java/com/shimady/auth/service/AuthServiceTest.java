package com.shimady.auth.service;

import com.shimady.auth.model.Group;
import com.shimady.auth.model.User;
import com.shimady.auth.model.dto.JwtResponse;
import com.shimady.auth.model.dto.SignInJwtRequest;
import com.shimady.auth.model.dto.SignUpJwtRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private GroupService groupService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    @InjectMocks
    private AuthService authService;

    @Test
    void shouldGetCurrentUser() {
        var user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("test@example.com");
        user.setPassword("password");
        var group = new Group();
        group.setId(1L);
        group.setName("Test Group");
        user.setGroup(group);

        given(userService.getUserByEmail(anyString())).willReturn(user);
        willReturn(user.getEmail()).given(authService).getUserEmail();

        var response = authService.getCurrentUser();

        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getFirstName(), response.getFirstName());
        assertEquals(user.getLastName(), response.getLastName());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getGroup().getName(), response.getGroupName());
        assertEquals(user.getGroup().getId(), response.getGroupId());
        then(userService).should().getUserByEmail(user.getEmail());
    }

    @Test
    void shouldSignUpUser() {
        var request = new SignUpJwtRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setGroupId(1L);
        var group = new Group();
        group.setId(1L);
        group.setName("Test Group");

        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword");
        given(jwtService.generateTokens(any(User.class))).willReturn(new JwtResponse("token", "refreshToken"));
        given(groupService.getGroupById(request.getGroupId())).willReturn(group);

        var response = authService.signUp(request);

        assertNotNull(response);
        assertEquals("token", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        then(groupService).should().getGroupById(request.getGroupId());
        then(userService).should().saveUser(any(User.class));
    }

    @Test
    void shouldAuthenticateUser() {
        var request = new SignInJwtRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        var user = new User();
        user.setId(1L);
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword");

        given(userService.getUserByEmail(request.getEmail())).willReturn(user);
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(true);
        given(jwtService.generateTokens(user)).willReturn(new JwtResponse("token", "refreshToken"));

        var response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("token", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void shouldThrowExceptionWhenUserPasswordIsInvalid() {
        var request = new SignInJwtRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");
        var user = new User();
        user.setId(1L);
        user.setPassword("encodedPassword");

        given(userService.getUserByEmail(request.getEmail())).willReturn(user);
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.authenticate(request));
    }

    @Test
    void shouldRefreshToken() {
        var refreshToken = "refreshToken";

        given(jwtService.refreshToken(refreshToken)).willReturn(new JwtResponse("newToken", "newRefreshToken"));

        var response = authService.refreshToken(refreshToken);

        assertNotNull(response);
        assertEquals("newToken", response.getAccessToken());
        assertEquals("newRefreshToken", response.getRefreshToken());
    }

    @Test
    void shouldLogout() {
        var email = "test@example.com";

        jwtService.deleteTokenByEmail(email);

        then(jwtService).should().deleteTokenByEmail(email);
    }
}
