package com.example.demo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Long createPost() {
        User user = new User();
        user.setUsername("test");
        userRepository.save(user);

        Post post = new Post();
        post.setTitle("Title");
        post.setContent("Content");
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }

    public List<Post> getPosts() {
        List<Post> posts = postRepository.findAll();
        return posts;
    }
}
