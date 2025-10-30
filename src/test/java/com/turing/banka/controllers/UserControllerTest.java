package com.turing.banka.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turing.banka.config.TestSecurityConfig;
import com.turing.banka.configurations.SecurityConfig;
import com.turing.banka.dtos.UserRequest;
import com.turing.banka.dtos.UserResponse;
import com.turing.banka.dtos.UserUpdateRequest;
import com.turing.banka.models.User;
import com.turing.banka.services.UserService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ShouldReturnCreatedUser() throws Exception {
        UserRequest request = new UserRequest("Jane", "Doe", "jane@example.com", "StrongP@ss1", "+15551234567");

        UserResponse response = new UserResponse(UUID.randomUUID(), "Jane", "Doe", "jane@example.com", LocalDateTime.now());

        when(userService.registerUser(any(UserRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("jane@example.com"))
            .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void register_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setFirstName(""); // invalid blank name
        invalidRequest.setEmail("not-an-email"); // invalid email
        invalidRequest.setPassword("weak"); // invalid password

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void register_ShouldReturnCreatedUserWithUsername() throws Exception {
        UserRequest request = new UserRequest("Jane", "Doe", "jane@example.com", "StrongP@ss1", "+15551234567");
        request.setUsername("JaneDoe");

        UserResponse response = new UserResponse(UUID.randomUUID(), "Jane", "Doe", "jane@example.com", LocalDateTime.now());
        response.setUsername("JaneDoe");

        when(userService.registerUser(any(UserRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("jane@example.com"))
            .andExpect(jsonPath("$.firstName").value("Jane"))
            .andExpect(jsonPath("$.username").value("JaneDoe"));
    }

    @Test
    void shouldUpdateUsernameSuccessfully() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setUsername("username");

        UserResponse response = new UserResponse(UUID.randomUUID(), "Jane", "Doe", "jane@example.com", LocalDateTime.now());
        response.setUsername("username");

        when(userService.updateUsername(Mockito.eq("uuid"), any(UserUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/users/uuid/username")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("username"));
    }
}
