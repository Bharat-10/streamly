package com.bharat.streamly.service;

import com.bharat.streamly.document.PostDocument;
import com.bharat.streamly.document.UserDocument;
import com.bharat.streamly.entity.PostType;
import com.bharat.streamly.repository.PostRepository;
import com.bharat.streamly.repository.PostSearchRepository;
import com.bharat.streamly.repository.UserRepository;
import com.bharat.streamly.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchSync implements CommandLineRunner {
    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;

    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;


    @Override
    public void run(String... args) throws Exception {
        List<PostDocument> postDocuments = postRepository.findAll().stream()
                .filter(post -> post.getType() != PostType.STORY)
                .map(post -> new PostDocument(post.getId(), post.getTitle(), post.getDescription()))
                .toList();
        postSearchRepository.saveAll(postDocuments);

        List<UserDocument> userDocuments = userRepository.findAll().stream()
                .map(user -> new UserDocument(user.getId(), user.getUsername()))
                .toList();
        userSearchRepository.saveAll(userDocuments);
    }
}
