package com.turing.banka.controllers;

import com.turing.banka.dtos.UserRequest;
import com.turing.banka.dtos.UserResponse;
import com.turing.banka.dtos.UserUpdateRequest;
import com.turing.banka.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}/username")
    public ResponseEntity<UserResponse> updateUsername(@PathVariable String id,
                                                       @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUsername(id, request));
    }
}
