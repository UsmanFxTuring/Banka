package com.turing.banka.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turing.banka.config.TestSecurityConfig;
import com.turing.banka.dtos.UserRequest;
import com.turing.banka.dtos.UserResponse;
import com.turing.banka.services.AuthService;
import com.turing.banka.services.UserService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ShouldReturnCreatedUser() throws Exception {
        UserRequest request = new UserRequest("Jane", "Doe", "jane@example.com", "StrongP@ss1", "+15551234567");

        UserResponse response = new UserResponse(UUID.randomUUID(), "Jane", "Doe", "jane@example.com", LocalDateTime.now());

        when(authService.registerUser(any(UserRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("jane@example.com"))
            .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void register_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setFirstName("");
        invalidRequest.setEmail("not-an-email");
        invalidRequest.setPassword("weak");

        mockMvc.perform(post("/api/v1/auth/register")
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

        when(authService.registerUser(any(UserRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("jane@example.com"))
            .andExpect(jsonPath("$.firstName").value("Jane"))
            .andExpect(jsonPath("$.username").value("JaneDoe"));
    }
}
