package com.bharat.streamly.service;

import com.bharat.streamly.document.UserDocument;
import com.bharat.streamly.dto.FollowResponse;
import com.bharat.streamly.dto.ProfileResponse;
import com.bharat.streamly.dto.UserPageResponse;
import com.bharat.streamly.dto.UserSummaryResponse;
import com.bharat.streamly.entity.Follow;
import com.bharat.streamly.entity.PostType;
import com.bharat.streamly.entity.User;
import com.bharat.streamly.exception.InvalidFollowException;
import com.bharat.streamly.exception.UserNotFoundException;
import com.bharat.streamly.repository.FollowRepository;
import com.bharat.streamly.repository.PostRepository;
import com.bharat.streamly.repository.UserRepository;
import com.bharat.streamly.repository.UserSearchRepository;
import com.bharat.streamly.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final UserSearchRepository userSearchRepository;

    public User currentUser() {
        return userRepository.findById(SecurityUtils.currentUserId()).orElseThrow();
    }

    public FollowResponse follow(String username) {
        User target = getByUsername(username);
        if (target.getId().equals(SecurityUtils.currentUserId())) {
            throw new InvalidFollowException("You can't follow yourself");
        }
        Follow follow = new Follow();
        follow.setFollower(currentUser());
        follow.setFollowee(target);
        try {
            followRepository.save(follow);
        } catch (DataIntegrityViolationException ignored) {
        }
        return new FollowResponse(followRepository.countByFolloweeId(target.getId()), true);
    }


    public FollowResponse unfollow(String username) {
        User target = getByUsername(username);
        Optional<Follow> follow = followRepository.findByFollowerIdAndFolloweeId(SecurityUtils.currentUserId(), target.getId());
        if (follow.isPresent()) {
            followRepository.delete(follow.get());
        }
        return new FollowResponse(followRepository.countByFolloweeId(target.getId()), false);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public ProfileResponse profile(String username) {
        User user = getByUsername(username);
        return new ProfileResponse(user.getId(),
                user.getUsername(),
                postRepository.countByUserIdAndTypeNot(user.getId(), PostType.STORY),
                followRepository.countByFolloweeId(user.getId()),
                followRepository.countByFollowerId(user.getId()),
                followRepository.existsByFollowerIdAndFolloweeId(SecurityUtils.currentUserId(), user.getId()));
    }

    public UserPageResponse followers(String username, int page, int size) {
        User user = getByUsername(username);
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), Sort.by("createdAt").descending());
        Page<Follow> result = followRepository.findByFolloweeId(user.getId(), pageable);
        List<UserSummaryResponse> users = result.getContent().stream()
                .map(follow -> new UserSummaryResponse(follow.getFollower().getId(), follow.getFollower().getUsername()))
                .toList();
        return new UserPageResponse(users, page, result.hasNext());
    }

    public UserPageResponse following(String username, int page, int size) {
        User user = getByUsername(username);
        Pageable pageable = PageRequest.of(page, Math.min(size, 50), Sort.by("createdAt").descending());
        Page<Follow> result = followRepository.findByFollowerId(user.getId(), pageable);
        List<UserSummaryResponse> users = result.getContent().stream()
                .map(follow -> new UserSummaryResponse(follow.getFollowee().getId(), follow.getFollowee().getUsername()))
                .toList();
        return new UserPageResponse(users, page, result.hasNext());
    }

    public UserPageResponse search(String q, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 50));
        Page<UserDocument> result = userSearchRepository.search(q, pageable);
        List<UserSummaryResponse> users = result.getContent().stream()
                .map(user -> new UserSummaryResponse(user.getId(), user.getUsername()))
                .toList();
        return new UserPageResponse(users, page, result.hasNext());
    }
}