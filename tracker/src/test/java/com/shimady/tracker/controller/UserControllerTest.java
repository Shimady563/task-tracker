package com.shimady.tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shimady.tracker.config.TestSecurityConfig;
import com.shimady.tracker.model.dto.UserDTO;
import com.shimady.tracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithAnonymousUser
    public void shouldSignUpUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Username");
        userDTO.setEmail("email@email.com");
        userDTO.setPassword("Password12345^");
        userDTO.setPhoneNumber("+76969696969");

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());

        then(userService).should().createUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser
    public void shouldUpdateUser() throws Exception {
        Long id = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Username");
        userDTO.setEmail("email@email.com");
        userDTO.setPassword("NewPassword123#");
        userDTO.setPhoneNumber("+76969696969");

        mockMvc.perform(put("/users/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNoContent());

        then(userService).should().updateUser(eq(id), any(UserDTO.class));
    }
}
