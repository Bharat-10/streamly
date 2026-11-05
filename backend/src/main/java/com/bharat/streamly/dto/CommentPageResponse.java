package com.bharat.streamly.dto;

import java.util.List;

public record CommentPageResponse(List<CommentResponse> comments, int page, boolean hasNext) {
}
