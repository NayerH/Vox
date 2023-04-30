package com.vox.post.service.commands;

import com.vox.post.model.Post;
import com.vox.post.repository.PostRepository;
import com.vox.post.service.interfaces.ReturnManyCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetTopPostsInCategoriesCommand implements ReturnManyCommand {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public GetTopPostsInCategoriesCommand(@Qualifier("redisScheduleTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public List<Post> execute(Object o){
        Object topPostsInAllCategories = redisTemplate.opsForValue().get("topPostsInAllCategories");
        return (List<Post>) topPostsInAllCategories;
    }

}
