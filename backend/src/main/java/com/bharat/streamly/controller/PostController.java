package com.bharat.streamly.controller;


import com.bharat.streamly.dto.*;
import com.bharat.streamly.entity.PostType;
import com.bharat.streamly.service.PostService;
import com.bharat.streamly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(
            @RequestParam("type") PostType type,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile media,
            @RequestParam(required = false) MultipartFile thumbnail
    ) {
        PostResponse postResponse = postService.create(type, title, description, media, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @GetMapping("/{id}")
    public PostResponse getById(@PathVariable("id") Long id) {
        return postService.getById(id);
    }

    @GetMapping
    public PostPageResponse feed(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return postService.feed(page, size);
    }

    @PostMapping("/{id}/like")
    public LikeResponse like(@PathVariable("id") Long id) {
        return postService.like(id);
    }

    @DeleteMapping("/{id}/like")
    public LikeResponse unlike(@PathVariable("id") Long id) {
        return postService.unlike(id);
    }

    @GetMapping("/reels")
    public PostPageResponse reels(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return postService.reels(page, size);
    }

    @GetMapping("/stories")
    public List<StoryGroupResponse> stories(){
        return postService.stories();
    }


}
