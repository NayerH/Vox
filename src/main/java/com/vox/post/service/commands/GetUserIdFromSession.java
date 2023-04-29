package com.vox.post.service.commands;

import com.vox.post.exception.ApiUnauthorizedException;
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
        Object userId = redisTemplate.opsForValue().get(sessionId+"userId");
        if(userId == null)
            throw new ApiUnauthorizedException("Invalid session ID");
        return (String) userId;
    }
}
