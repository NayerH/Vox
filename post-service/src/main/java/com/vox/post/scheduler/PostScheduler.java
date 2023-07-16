package com.vox.post.scheduler;

import com.vox.post.model.Post;

import com.vox.post.repository.posts.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PostScheduler {

    private final RedisCacheManager cacheManager;
    private final PostRepository postRepository;

    @Autowired
    public PostScheduler(PostRepository postRepository,@Qualifier("redisSchedulerCacheManager")  RedisCacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.postRepository = postRepository;
    }

    @Scheduled(cron = "0 0 * * * *") //runs every one hour at the beginning of the hour
    public void fetchTopPostsInCategories(){
        LocalDateTime maxDate = LocalDateTime.now().minusMonths(1);
        List<Post> topPostsInCategories = postRepository.findTopPostsInEachCategory(3, maxDate);
        cacheManager.getCache("top").put("topPostsInAllCategories", topPostsInCategories);
    }

}
