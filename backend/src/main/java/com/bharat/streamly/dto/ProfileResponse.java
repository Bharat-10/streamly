package com.bharat.streamly.dto;

public record ProfileResponse(
        Long id,
        String username,
        long postCount,
        long followerCount,
        long followingCount,
        boolean followedByMe
) {
}
