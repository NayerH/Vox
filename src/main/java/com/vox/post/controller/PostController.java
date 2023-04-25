package com.vox.post.controller;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "posts", key = "#id", unless = "#result.views < 1000") //Cache posts on request with views > 1000
    @GetMapping("/{id}")
    public Post getPost(@PathVariable String id) {
        return postService.getPost(id);
    }

    @PostMapping
    @CachePut(value = "posts", key = "#post.id") //Cache newly added posts
    public Post addPost(HttpSession session, @Validated @RequestBody Post post) {
        return postService.addPost(session.getId(), post);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "posts", key = "#id") //Evict cached posts by id
    public void deletePost(HttpSession session, @PathVariable String id) {
        postService.deletePost(session.getId(), id);
    }

//    TODO: Add Update Functionality
    @PutMapping("/{id}")
    @CachePut(value = "posts", key = "#post.id") //Cache recently updated posts
    public Post updatePost(HttpSession session, @PathVariable String id, @Validated @RequestBody Post post) {
        return postService.updatePost(session.getId(), id, post);
    }

    @GetMapping("/categories")
    @CachePut(value = "posts", key = "#category") //Cache top 3 posts in a category
    public List<Post> getTopPosts(@RequestParam("category") Category.CategoryEnum category) {
        return postService.getTopPostsInCategory(category);
    }

}