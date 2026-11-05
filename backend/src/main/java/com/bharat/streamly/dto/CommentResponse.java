package com.bharat.streamly.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String text,
        Long userId,
        LocalDateTime createdAt,
        String username
) {
}
