package com.vox.post.scheduler;

import com.vox.post.model.Post;
import com.vox.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//@Qualifier("redisTemplate")
public class PostScheduler {
    @Autowired
    private PostService postService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PostScheduler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(cron = "0 0 * * * *") //runs every one hour at the beginning of the hour
    public void fetchTopPostsInCategories(){
        List<Post> topPostsInCategories = postService.getTopPostsInCategories();
        redisTemplate.opsForValue().set("topPostsInAllCategories", topPostsInCategories);
    }

}
