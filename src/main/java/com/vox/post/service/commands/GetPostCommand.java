package com.vox.post.service.commands;

import com.vox.post.exception.ApiRequestException;
import com.vox.post.model.Post;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
public class GetPostCommand implements ReturnOneCommand {
    private final PostRepository postRepository;
    private CacheManager cacheManager;

    @Autowired
    public GetPostCommand(@Qualifier("redisCacheManager") CacheManager cacheManager, PostRepository postRepository) {
        this.cacheManager = cacheManager;
        this.postRepository = postRepository;

    }

    @Override
    public Post execute(Object o) {
        String id = (String) o;
        Post cachedPost = cacheManager.getCache("posts").get(id, Post.class);
        if(cachedPost != null){
            cachedPost.setViews(cachedPost.getViews() + 1);
            cacheManager.getCache("posts").put(id, cachedPost);
            this.postRepository.save(cachedPost);
            return cachedPost;
        }

        Optional<Post> optionalPost = postRepository.findById(id);
        if(!optionalPost.isPresent()){
            throw new ApiRequestException("No post is available with id " + id);
        }
        Post p = optionalPost.get();
        p.setViews(p.getViews() + 1);
        this.postRepository.save(p);
        long MAX_VIEWS = 1000000L;
        if (p.getViews() >= MAX_VIEWS && p.getPublishedAt().after(Date.from(Instant.now().minus(30, ChronoUnit.DAYS)))) {
            cacheManager.getCache("posts").put(id, p);
        }
        return p;
    }
}
