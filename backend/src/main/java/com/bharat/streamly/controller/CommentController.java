package com.bharat.streamly.controller;

import com.bharat.streamly.dto.CommentPageResponse;
import com.bharat.streamly.dto.CommentRequest;
import com.bharat.streamly.dto.CommentResponse;
import com.bharat.streamly.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> create(@PathVariable("postId") Long postId,
                                                  @Valid @RequestBody CommentRequest request) {
        CommentResponse commentResponse = commentService.create(postId, request.text());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }

    @GetMapping
    public CommentPageResponse list(@PathVariable("postId") Long postId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size) {
        return commentService.list(postId, page, size);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

}
