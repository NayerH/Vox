package com.vox.post.service.commands;

import com.vox.post.model.Post;
import com.vox.post.service.interfaces.ReturnManyCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetTopPostsInCategoriesCommand implements ReturnManyCommand {

    private final RedisCacheManager cacheManager;

    @Autowired
    public GetTopPostsInCategoriesCommand(@Qualifier("redisSchedulerCacheManager")  RedisCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    @Override
    public List<Post> execute(Object o){
        List<Post> topPostsInAllCategories = cacheManager.getCache("top").get("topPostsInAllCategories", List.class);
        return topPostsInAllCategories;
    }

}
