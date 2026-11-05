package com.bharat.streamly.dto;

import java.util.List;

public record PostPageResponse(List<PostResponse> posts, int page, boolean hasNext) {
}
