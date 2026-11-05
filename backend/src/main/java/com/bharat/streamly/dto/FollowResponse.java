package com.bharat.streamly.dto;

public record FollowResponse(
        long followerCount,
        boolean followedByMe
) {
}
