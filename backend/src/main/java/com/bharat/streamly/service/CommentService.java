package com.bharat.streamly.service;

import com.bharat.streamly.dto.CommentPageResponse;
import com.bharat.streamly.dto.CommentResponse;
import com.bharat.streamly.dto.PostPageResponse;
import com.bharat.streamly.dto.PostResponse;
import com.bharat.streamly.entity.Comment;
import com.bharat.streamly.entity.Post;
import com.bharat.streamly.entity.PostType;
import com.bharat.streamly.exception.CommentNotFoundException;
import com.bharat.streamly.exception.ForbiddenException;
import com.bharat.streamly.exception.PostNotFoundException;
import com.bharat.streamly.repository.CommentRepository;
import com.bharat.streamly.repository.PostRepository;
import com.bharat.streamly.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentResponse create(Long postId, String text) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        Comment comment = new Comment();
        comment.setUser(userService.currentUser());
        comment.setPost(post);
        comment.setText(text);
        return toCommentResponse(commentRepository.save(comment));
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getUser().getId(),
                comment.getCreatedAt(),
                comment.getUser().getUsername()
        );
    }

    public CommentPageResponse list(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), Sort.by("createdAt").descending());
        Page<Comment> result = commentRepository.findByPostId(postId, pageable);
        List<CommentResponse> commentResponses = result.getContent().stream().map(this::toCommentResponse).toList();
        return new CommentPageResponse(commentResponses, page, result.hasNext());
    }

    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment not found"));
        if (!comment.getUser().getId().equals(SecurityUtils.currentUserId())) {
            throw new ForbiddenException("You can only delete your comment");
        }
        commentRepository.delete(comment);
    }

}
