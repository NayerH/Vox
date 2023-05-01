package com.vox.post.service.commands;

import com.vox.post.exception.ApiRequestException;
import com.vox.post.model.Post;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
public class GetPostCommand implements ReturnOneCommand {
    private final RedisTemplate<String, Object> redisTemplate;
    private final PostRepository postRepository;
    private final LocalDateTime MAX_DATE = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);


    @Autowired
    public GetPostCommand(RedisTemplate<String, Object> redisTemplate, PostRepository postRepository) {
        this.redisTemplate = redisTemplate;
        this.postRepository = postRepository;

    }

    @Override
    public Post execute(Object o) {
        Post cachedPost = (Post) redisTemplate.opsForValue().get((String) o);
        if(cachedPost != null){
            System.out.println("Post found in cache");
            return cachedPost;
        } else {
            System.out.println("Post not found in cache");
        }

        String m = (String) o;
        Optional<Post> optionalPost = postRepository.findById(m);
        if(optionalPost.isPresent()){
            Post p = optionalPost.get();
            p.setViews(p.getViews() + 1);
            this.postRepository.save(p);
            long MAX_VIEWS = 1000000L;
            if (p.getViews() >= MAX_VIEWS && p.getPublishedAt().after(Date.from(Instant.now().minus(30, ChronoUnit.DAYS)))) {
                redisTemplate.opsForValue().set((String) o, p);
            }
            return p;
        }
        throw new ApiRequestException("No post is available with id " + m);
    }
}
