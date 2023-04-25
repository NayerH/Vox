package com.vox.post.controller;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
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
    @CachePut(value = "posts", key = "#post.id") //Cache newly added posts
    public Post addPost(HttpSession session, @Validated @RequestBody Post post) {
        return postService.addPost(session.getId(), post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(HttpSession session, @PathVariable MongoId id) {
        postService.deletePost(session.getId(), id);
    }

    @PutMapping("/{id}")
    @CachePut(value = "posts", key = "#{id}") //Cache recently updated posts
    public Post updatePost(
            HttpSession session,
            @PathVariable MongoId id,
            @RequestBody(required = false) String title,
            @RequestBody(required = false) String content,
            @RequestBody(required = false) List<String> tags,
            @RequestBody(required = false) Category.CategoryEnum category,
            @RequestBody(required = false) List<Long> mediaFiles
    ) {
        return postService.updatePost(
                session.getId(),
                id,
                title,
                content,
                tags,
                category,
                mediaFiles
        );
    }

    @GetMapping("/{category}/top")
    @CachePut(value = "topPosts", key = "#category") //Cache top 3 posts in a category
    public List<Post> getTopPosts(@PathVariable Category.CategoryEnum category) {
        return postService.getTopPostsInCategory(category);
    }

    @GetMapping("/{category}")
    public List<Post> getCategoryPosts(
            @PathVariable Category.CategoryEnum category,
            @RequestBody(required = false) Integer skip
    ) {
        return postService.getCategoryPosts(category, skip);
    }

}