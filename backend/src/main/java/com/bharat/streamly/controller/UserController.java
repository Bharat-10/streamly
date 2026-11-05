package com.bharat.streamly.controller;

import com.bharat.streamly.dto.*;
import com.bharat.streamly.entity.User;
import com.bharat.streamly.service.PostService;
import com.bharat.streamly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/me")
    public UserResponse me() {
        User user = userService.currentUser();
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
    }

    @PostMapping("/{username}/follow")
    public FollowResponse follow(@PathVariable("username") String username) {
        return userService.follow(username);
    }

    @DeleteMapping("/{username}/follow")
    public FollowResponse unfollow(@PathVariable("username") String username) {
        return userService.unfollow(username);
    }

    @GetMapping("/{username}")
    public ProfileResponse profile(@PathVariable("username") String username) {
        return userService.profile(username);
    }

    @GetMapping("/{username}/posts")
    public PostPageResponse posts(@PathVariable("username") String username,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return postService.userPosts(username, page, size);
    }

    @GetMapping("/{username}/followers")
    public UserPageResponse followers(@PathVariable("username") String username,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return userService.followers(username, page, size);
    }

    @GetMapping("/{username}/following")
    public UserPageResponse following(@PathVariable("username") String username,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return userService.following(username, page, size);
    }
}
