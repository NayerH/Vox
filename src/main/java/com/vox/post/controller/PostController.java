package com.vox.post.controller;

import com.vox.post.model.Post;
import com.vox.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/posts")
public class PostController {

    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable MongoId id) {
        return postService.getPost(id);
    }

    @PostMapping
    public Post addPost(@Validated @RequestBody Post post) {
        return postService.addPost(post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable MongoId id) {
        postService.deletePost(id);
    }

}