package com.vox.post.controller;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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


//    Returns all posts
    @GetMapping
    public List<Post> getPosts() {
        return postService.getPosts();
    }

//    Return a post by id and cache it if it has more than 1000 views
    @Cacheable(value = "posts", key = "#id", unless = "#result.views < 1000")
    @GetMapping("/{id}")
    public Post getPost(@PathVariable String id) {
        return postService.getPost(id);
    }

    @PostMapping
    @CachePut(value = "posts", key = "#post.id") //Cache newly added posts
    public Post addPost(HttpSession session, @Validated @RequestBody Post post) {
//        TODO: Add Media Server
        return postService.addPost(session.getId(), post);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "posts", key = "#id") //Evict cached posts by id
    public void deletePost(HttpSession session, @PathVariable String id) {
        postService.deletePost(session.getId(), id);
    }


//    Returns top 3 posts in a category and caches the result after each request
    @GetMapping("/categories")
    @CachePut(value = "posts", key = "#category") //Cache top 3 posts in a category
    public List<Post> getTopPosts(@RequestParam("category") Category.CategoryEnum category) {
        return postService.getTopPostsInCategory(category);
    }

//    Returns top posts in all categories from cache & is updated every hour
    @GetMapping("/categories/top")
    public List<Post> getTopPostsPerCategories() {
        return postService.getTopPostsInCategories();
    }

    @PutMapping("/{id}")
    @CachePut(value = "posts", key = "#{id}") //Cache recently updated posts
    public Post updatePost(
            HttpSession session,
            @PathVariable String id,
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

    @GetMapping("/{category}")
    public List<Post> getCategoryPosts(
            @PathVariable Category.CategoryEnum category,
            @RequestBody(required = false) Integer skip
    ) {
        return postService.getCategoryPosts(category, skip);
    }

}