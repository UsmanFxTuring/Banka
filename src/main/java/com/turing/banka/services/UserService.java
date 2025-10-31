package com.turing.banka.services;

import com.turing.banka.dtos.UserRequest;
import com.turing.banka.dtos.UserResponse;
import com.turing.banka.dtos.UserUpdateRequest;
import com.turing.banka.exceptions.CustomException;
import com.turing.banka.models.User;
import com.turing.banka.repositories.UserRepository;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse updateUsername(String userId, UserUpdateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException("Username already taken", HttpStatus.CONFLICT);
        }

        User user = userRepository.findById(UUID.fromString(userId))
            .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        user.setUsername(request.getUsername());
        user = userRepository.save(user);

        final var userResponse = new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
            user.getCreatedAt());
        userResponse.setUsername(request.getUsername());
        return userResponse;
    }
}
