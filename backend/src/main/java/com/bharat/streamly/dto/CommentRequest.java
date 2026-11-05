package com.bharat.streamly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "text is required")
        @Size(max = 500, message = "text must be at most 500 characters")
        String text
) {}