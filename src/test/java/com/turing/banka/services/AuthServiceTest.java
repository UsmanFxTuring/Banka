package com.turing.banka.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.turing.banka.dtos.UserRequest;
import com.turing.banka.dtos.UserResponse;
import com.turing.banka.exceptions.CustomException;
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
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    AuthService authService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new UserRequest("John", "Doe", "john@example.com", "StrongP@ss1", "+15551234567");
    }

    @Test
    void registerUserShouldCreateUserWhenEmailIsNew() {
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(modelMapper.map(any(UserRequest.class), eq(User.class))).thenReturn(new User(UUID.randomUUID(), request.getFirstName(),
            request.getLastName(), request.getEmail(), request.getPassword(), request.getPhoneNumber(), LocalDateTime.now()));
        when(userRepository.save(any(User.class)))
            .thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setCreatedAt(LocalDateTime.now());
                return user;
            });

        UserResponse response = authService.registerUser(request);

        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.findByEmail(request.getEmail()))
            .thenReturn(Optional.of(new User()));

        assertThrows(CustomException.class, () -> authService.registerUser(request));

        verify(userRepository, never()).save(any(User.class));
    }
}
