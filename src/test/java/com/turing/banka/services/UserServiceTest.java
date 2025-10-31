package com.turing.banka.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.turing.banka.dtos.UserRequest;
import com.turing.banka.dtos.UserUpdateRequest;
import com.turing.banka.models.User;
import com.turing.banka.repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new UserRequest("John", "Doe", "john@example.com", "StrongP@ss1", "+15551234567");
    }

    @Test
    void updateUsernameShouldBeSuccessful() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        final var userId = UUID.randomUUID();
        final var user = new User(userId, "firstName", "lastName", "email", "password", "phoneNumber", LocalDateTime.now());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        final var updateRequest = new UserUpdateRequest();
        request.setUsername("username");
        final var response = userService.updateUsername(userId.toString(), updateRequest);
        assertNotNull(response);
    }
}
