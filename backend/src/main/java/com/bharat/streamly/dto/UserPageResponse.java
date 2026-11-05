package com.bharat.streamly.dto;

import java.util.List;

public record UserPageResponse(List<UserSummaryResponse> users, int page, boolean hasNext) {
}
