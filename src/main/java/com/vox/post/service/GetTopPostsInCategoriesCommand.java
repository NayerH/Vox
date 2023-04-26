package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.repository.PostRepository;
import com.vox.post.service.interfaces.ReturnManyCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetTopPostsInCategoriesCommand implements ReturnManyCommand {

    @Autowired
    private PostRepository postRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public GetTopPostsInCategoriesCommand(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public List<Post> execute(Object o){
        Object topPostsInAllCategories = redisTemplate.opsForValue().get("topPostsInAllCategories");
        return (List<Post>) topPostsInAllCategories;
    }

}
