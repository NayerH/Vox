package com.vox.post.controller;

import com.vox.post.controller.request.RequestWrapper;
import com.vox.post.model.*;
import com.vox.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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


//    QUESTION: Should we implement a method that caches most viewed (e.g. top 10) posts in general every hour?

//    Returns all posts
    @GetMapping("/get")
    public List<Post> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/get/{id}")
    public Post getPost(@PathVariable("id") String id) {
        return postService.getPost(id);
    }

    @PostMapping("/add")
    @CachePut(value = "posts", key = "#result.id") //Cache newly added posts
    public Post addPost(HttpSession session, @Validated @RequestBody RequestWrapper body) {
        Post post = body.getPost();
        List<MediaFile> mediaFiles = body.getMediaFiles();
        return postService.addPost(session.getId(), post, mediaFiles);
    }


    @DeleteMapping("/delete/{id}")
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
//    NOTE: The method won't update the cache ->
//     1. if a post is trending ATM but wasn't trending an hour ago until the next hour
//     2. if one of the cached posts is deleted or updated until the next hour
    @GetMapping("/categories/top")
    public List<Post> getTopPostsPerCategories() {
        return postService.getTopPostsInCategories();
    }

//    NOTE: This method will probably need a custom handler in order to retrieve the information from the request body
//      Similar to the custom handler in the addPost method
    @PutMapping("/update/{id}")
    @CachePut(value = "posts", key = "#{id}") //Cache recently updated posts
    public Post updatePost(
            HttpSession session,
            @PathVariable String id,
            @RequestBody(required = false) String title,
            @RequestBody(required = false) String content,
            @RequestBody(required = false) List<String> tags,
            @RequestBody(required = false) Category.CategoryEnum category,
            @RequestBody(required = false) String mediaFilesReference
    ) {
        return postService.updatePost(
                session.getId(),
                id,
                title,
                content,
                tags,
                category,
                mediaFilesReference
        );
    }

    @GetMapping("/categories/{category}")
    public List<Post> getCategoryPosts(
            @PathVariable Category.CategoryEnum category,
            @RequestBody(required = false) Integer skip
    ) {
        return postService.getCategoryPosts(category, skip);
    }

    @PutMapping(path = "/comments/{postId}")
    public void addComment(HttpSession session, @PathVariable("postId") String postId, @RequestBody Comment comment) {
        postService.addComment(session.getId(), postId, comment);
    }

    @PutMapping(path = "/comments/{postId}/{commentId}")
    public void addReply(HttpSession session, @PathVariable("postId") String postId, @PathVariable("commentId") String commentId, @RequestBody Comment reply) {
        postService.addReply(session.getId(), postId, commentId, reply);
    }
}