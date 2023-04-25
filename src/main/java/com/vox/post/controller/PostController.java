package com.vox.post.controller;

import com.vox.post.model.Post;
import com.vox.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/posts")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @Autowired
    public void setPostService(PostService postService) {
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
    public Post addPost(HttpSession session, @Validated @RequestBody Post post) {
        return postService.addPost(session.getId(), post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(HttpSession session, @PathVariable MongoId id) {
        postService.deletePost(session.getId(), id);
    }
}