package com.bharat.streamly.repository;

import com.bharat.streamly.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    long countByFolloweeId(Long followeeId);
    long countByFollowerId(Long followerId);

    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    Page<Follow> findByFolloweeId(Long followeeId, Pageable pageable);

    Page<Follow> findByFollowerId(Long followerId, Pageable pageable);

    List<Follow> findByFollowerId(Long followerId);
}
