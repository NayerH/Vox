package com.vox.post.service;

import com.vox.post.service.interfaces.ReturnIdCommand;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class GetUserIdFromSession implements ReturnIdCommand {

    private final RedisTemplate<String, Object> redisTemplate;

    public GetUserIdFromSession(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String execute(String sessionId) {
        Object userId = redisTemplate.opsForValue().get(sessionId);
        if(userId == null)
            throw new IllegalStateException("Invalid session ID");
        return (String) userId;
    }
}
