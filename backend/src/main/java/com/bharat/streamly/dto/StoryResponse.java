package com.bharat.streamly.dto;

import java.time.LocalDateTime;

public record StoryResponse(
        Long id,
        String mediaUrl,
        LocalDateTime createdAt,
        String contentType
) {
}
