package com.bharat.streamly.controller;

import com.bharat.streamly.dto.PostPageResponse;
import com.bharat.streamly.dto.UserPageResponse;
import com.bharat.streamly.service.PostService;
import com.bharat.streamly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/posts")
    public PostPageResponse posts(@RequestParam("q") String q,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return postService.search(q, page, size);
    }

    @GetMapping("/users")
    public UserPageResponse users(@RequestParam("q") String q,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size) {
        return userService.search(q, page, size);
    }
}