package com.turing.banka.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserUpdateRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_]{3,20}$", message = "Username must be 3–20 characters and alphanumeric")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
