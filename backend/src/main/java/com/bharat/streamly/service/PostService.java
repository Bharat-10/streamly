package com.bharat.streamly.service;

import com.bharat.streamly.document.PostDocument;
import com.bharat.streamly.dto.*;
import com.bharat.streamly.entity.*;
import com.bharat.streamly.exception.InvalidUploadException;
import com.bharat.streamly.exception.PostNotFoundException;
import com.bharat.streamly.repository.*;
import com.bharat.streamly.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;
    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    private final PostSearchRepository postSearchRepository;

    public PostResponse create(PostType type,
                               String title,
                               String description,
                               MultipartFile media,
                               MultipartFile thumbnail) {

        if (media == null || media.isEmpty()) {
            throw new InvalidUploadException("Media file is required");
        }
        if ((type == PostType.VIDEO || type == PostType.REEL)
                && (thumbnail == null || thumbnail.isEmpty())) {
            throw new InvalidUploadException("Thumbnail is required for video and reel");
        }
        Post post = new Post();
        post.setUser(userService.currentUser());
        post.setType(type);
        post.setTitle(title);
        post.setDescription(description);
        post.setMediaKey(s3Service.upload(media, "posts"));
        if (type == PostType.VIDEO || type == PostType.REEL) {
            post.setThumbnailKey(s3Service.upload(thumbnail, "thumbnails"));
        }
        if (type == PostType.STORY) {
            post.setExpiresAt(LocalDateTime.now().plusHours(24));
        }
        Post saved = postRepository.save(post);
        if (type != PostType.STORY) {
            postSearchRepository.save(new PostDocument(saved.getId(), saved.getTitle(), saved.getDescription()));
        }
        return toPostResponse(saved);

    }

    private PostResponse toPostResponse(Post post) {
        return new PostResponse(
                post.getId(),
                post.getType(),
                post.getTitle(),
                post.getDescription(),
                s3Service.getPresignedUrl(post.getMediaKey()),
                post.getThumbnailKey() != null ? s3Service.getPresignedUrl(post.getThumbnailKey()) : null,
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getCreatedAt(),
                postLikeRepository.countByPostId(post.getId()),
                postLikeRepository.existsByUserIdAndPostId(SecurityUtils.currentUserId(), post.getId()),
                commentRepository.countByPostId(post.getId())
        );

    }

    public PostResponse getById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        return toPostResponse(post);
    }

    public PostPageResponse feed(int page, int size) {
        List<Follow> follows = followRepository.findByFollowerId(SecurityUtils.currentUserId());
        if (follows.isEmpty()) {
            return new PostPageResponse(List.of(), page, false);
        }
        List<Long> userIds = follows.stream().map(x -> x.getFollowee().getId()).toList();
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), Sort.by("createdAt").descending());
        Page<Post> result = postRepository.findByUserIdInAndTypeNot(userIds, PostType.STORY, pageable);
        List<PostResponse> postResponseList = result.getContent().stream().map(this::toPostResponse).toList();
        return new PostPageResponse(postResponseList, page, result.hasNext());
    }

    public LikeResponse like(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        User user = userService.currentUser();
        PostLike like = new PostLike();
        like.setUser(user);
        like.setPost(post);
        try {
            postLikeRepository.save(like);
        } catch (DataIntegrityViolationException ignored) {
        }
        return new LikeResponse(postLikeRepository.countByPostId(id), true);
    }

    public LikeResponse unlike(Long id) {
        Optional<PostLike> like = postLikeRepository.findByUserIdAndPostId(SecurityUtils.currentUserId(), id);
        if (like.isPresent()) {
            postLikeRepository.delete(like.get());
        }
        return new LikeResponse(postLikeRepository.countByPostId(id), false);
    }

    public PostPageResponse userPosts(String username, int page, int size) {
        User user = userService.getByUsername(username);
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), Sort.by("createdAt").descending());
        Page<Post> result = postRepository.findByUserIdAndTypeNot(user.getId(), PostType.STORY, pageable);
        List<PostResponse> postResponseList = result.getContent().stream().map(this::toPostResponse).toList();
        return new PostPageResponse(postResponseList, page, result.hasNext());
    }

    public PostPageResponse reels(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), Sort.by("createdAt").descending());
        Page<Post> result = postRepository.findByType(PostType.REEL, pageable);
        List<PostResponse> postResponseList = result.getContent().stream().map(this::toPostResponse).toList();
        return new PostPageResponse(postResponseList, page, result.hasNext());
    }

    public List<StoryGroupResponse> stories() {
        Long myId = SecurityUtils.currentUserId();
        List<Follow> follows = followRepository.findByFollowerId(myId);
        List<Long> userIds = new ArrayList<>(follows.stream().map(x -> x.getFollowee().getId()).toList());
        userIds.add(myId);
        List<Post> stories = postRepository.findByUserIdInAndTypeAndExpiresAtAfter(userIds, PostType.STORY, LocalDateTime.now(), Sort.by("CreatedAt").descending());
        Map<Long, StoryGroupResponse> groups = new LinkedHashMap<>();
        for (Post story : stories) {
            User user = story.getUser();
            if (!groups.containsKey(user.getId())) {
                groups.put(user.getId(), new StoryGroupResponse(user.getId(), user.getUsername(), new ArrayList<>()));
            }
            groups.get(user.getId()).stories().add(toStoryResponse(story));
        }
        return new ArrayList<>(groups.values());
    }

    private StoryResponse toStoryResponse(Post post) {
        return new StoryResponse(
                post.getId(),
                s3Service.getPresignedUrl(post.getMediaKey()),
                post.getCreatedAt(),
                s3Service.contentTypeOf(post.getMediaKey())
        );
    }

    public PostPageResponse search(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 50));
        Page<PostDocument> result = postSearchRepository.search(q, pageable);
        List<Long> ids = result.getContent().stream().map(PostDocument::getId).toList();
        Map<Long, Post> posts = new HashMap<>();
        for (Post post : postRepository.findAllById(ids)) {
            posts.put(post.getId(), post);
        }
        List<PostResponse> postResponseList = ids.stream().map(id -> toPostResponse(posts.get(id))).toList();
        return new PostPageResponse(postResponseList, page, result.hasNext());
    }
}
