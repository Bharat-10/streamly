package com.bharat.streamly.dto;

import java.util.List;

public record StoryGroupResponse(
        Long userId,
        String username,
        List<StoryResponse> stories
) {
}
