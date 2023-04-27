package com.vox.post.scheduler;

import com.vox.post.model.Post;
import com.vox.post.model.PostRepository;
import com.vox.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PostScheduler {

    @Autowired
    private PostRepository postRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public PostScheduler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(cron = "0 0 * * * *") //runs every one hour at the beginning of the hour
    public void fetchTopPostsInCategories(){
        LocalDateTime maxDate = LocalDateTime.now().minusMonths(1);
        List<Post> topPostsInCategories = postRepository.findTopPostsInEachCategory(3, maxDate);
        redisTemplate.opsForValue().set("topPostsInAllCategories", topPostsInCategories);
    }

}
