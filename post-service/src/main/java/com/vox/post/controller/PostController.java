package com.vox.post.controller;

import com.vox.post.controller.request.RequestWrapper;
import com.vox.post.controller.request.UpdateWrapper;
import com.vox.post.model.*;
import com.vox.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "api/v1/posts")
@ConditionalOnProperty(prefix = "disable", name = "post.controller", havingValue = "false", matchIfMissing = true)
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
    @GetMapping("/all")
    @Async
    public CompletableFuture<List<Post>> getPosts() {
        return CompletableFuture.completedFuture(postService.getPosts());
    }

    @GetMapping("/get/{id}")
    @Async
    public CompletableFuture<Post> getPost(@PathVariable("id") String id) {
        return CompletableFuture.completedFuture(postService.getPost(id));
    }

    @PostMapping("/add")
//    @CachePut(value = "posts", key = "#result.id") //Cache newly added posts
    @Async
    public void addPost(HttpSession session, @Validated @RequestBody RequestWrapper body) {
        Post post = body.getPost();
        Media media = body.getMedia();
        postService.addPost(session.getId(), post, media);
    }


    @DeleteMapping("/delete/{id}")
    @CacheEvict(value = "posts", key = "#id") //Evict cached posts by id
    @Async
    public void deletePost(HttpSession session, @PathVariable String id) {
        postService.deletePost(session.getId(), id);
    }

//    Returns top 3 posts in a category and caches the result after each request
    @GetMapping("/categories")
    @CachePut(value = "posts", key = "#category") //Cache top 3 posts in a category
    @Async
    public CompletableFuture<List<Post>> getTopPosts(@RequestParam("category") Category.CategoryEnum category) {
        return CompletableFuture.completedFuture(postService.getTopPostsInCategory(category));
    }

//    Returns top posts in all categories from cache & is updated every hour
//    NOTE: The method won't update the cache ->
//     1. if a post is trending ATM but wasn't trending an hour ago until the next hour
//     2. if one of the cached posts is deleted or updated until the next hour
    @GetMapping("/categories/top")
    @Async
    public CompletableFuture<List<Post>> getTopPostsPerCategories() {
        return CompletableFuture.completedFuture(postService.getTopPostsInCategories());
    }


    @PutMapping("/update/{id}")
    @CachePut(value = "posts", key = "#{id}") //Cache recently updated posts
    @Async
    public void updatePost(HttpSession session, @PathVariable String id, @RequestBody UpdateWrapper body) {
        String title = body.getTitle();
        String content = body.getContent();
        List<String> tags = body.getTags();
        Category.CategoryEnum category = body.getCategory();
        String mediaFilesReference = body.getMediaFilesReference();
        List<MediaFile> mediaFiles = body.getMediaFiles();

        postService.updatePost(
                session.getId(),
                id,
                title,
                content,
                tags,
                category,
                mediaFilesReference,
                mediaFiles
        );
    }

    @GetMapping("/categories/{category}")
    @Async
    public CompletableFuture<List<Post>> getCategoryPosts(
            @PathVariable Category.CategoryEnum category,
            @RequestBody(required = false) Integer skip
    ) {
        return CompletableFuture.completedFuture(postService.getCategoryPosts(category, skip));
    }

    @PutMapping(path = "/comments/{postId}")
    @Async
    public void addComment(HttpSession session, @PathVariable("postId") String postId, @RequestBody Comment comment) {
        postService.addComment(session.getId(), postId, comment);
    }

    @PutMapping(path = "/comments/{postId}/{commentId}")
    @Async
    public void addReply(HttpSession session, @PathVariable("postId") String postId, @PathVariable("commentId") String commentId, @RequestBody Comment reply) {
        postService.addReply(session.getId(), postId, commentId, reply);
    }
}