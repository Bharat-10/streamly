package com.bharat.streamly.repository;

import com.bharat.streamly.entity.Post;
import com.bharat.streamly.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTypeNot(PostType type, Pageable pageable);
    Page<Post> findByType(PostType type, Pageable pageable);

    long countByUserIdAndTypeNot(Long userId, PostType type);

    Page<Post> findByUserIdAndTypeNot(Long id, PostType postType, Pageable pageable);

    Page<Post> findByUserIdInAndTypeNot(List<Long> userIds, PostType postType, Pageable pageable);
    List<Post> findByUserIdInAndTypeAndExpiresAtAfter(List<Long> userIds, PostType postType, LocalDateTime now, Sort sort);
}