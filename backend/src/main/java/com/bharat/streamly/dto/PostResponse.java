package com.bharat.streamly.dto;


import com.bharat.streamly.entity.PostType;
import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        PostType type,
        String title,
        String description,
        String mediaUrl,
        String thumbnailUrl,
        Long uploaderId,
        String uploaderUsername,
        LocalDateTime createdAt,
        long likeCount,
        boolean likedByMe,
        long commentCount
) {
}