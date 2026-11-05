package com.bharat.streamly.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid email address")
        String email,

        @NotBlank(message = "username is required")
        @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$",
                message = "username can only have letters, numbers and underscore, and must be 3 to 20 characters")
        String username,

        @NotBlank(message = "password is required")
        @Size(min = 8, message = "password must be at least 8 characters")
        String password
) {}