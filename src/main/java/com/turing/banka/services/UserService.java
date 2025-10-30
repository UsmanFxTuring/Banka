package com.turing.banka.services;

import com.turing.banka.dtos.UserRequest;
import com.turing.banka.dtos.UserResponse;
import com.turing.banka.exceptions.CustomException;
import com.turing.banka.models.User;
import com.turing.banka.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UserResponse registerUser(UserRequest request) {
        userRepository.findByEmail(request.getEmail())
            .ifPresent(u -> {
                throw new CustomException("Email already registered: " + request.getEmail(), HttpStatus.CONFLICT);
            });

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getFirstName(), saved.getLastName(), saved.getEmail(), saved.getCreatedAt());
    }
}
