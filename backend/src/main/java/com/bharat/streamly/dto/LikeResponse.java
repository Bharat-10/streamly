package com.bharat.streamly.dto;

public record LikeResponse(
        long likeCount,
        boolean likedByMe
) {
}