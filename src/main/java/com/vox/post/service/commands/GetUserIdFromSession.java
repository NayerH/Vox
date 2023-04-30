package com.vox.post.service.commands;

import com.vox.post.exception.ApiUnauthorizedException;
import com.vox.post.service.interfaces.ReturnIdCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GetUserIdFromSession implements ReturnIdCommand {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public GetUserIdFromSession(@Qualifier("redisUserTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public String execute(String sessionId) {
        //TODO: Remove Hardcoded Value
        HashMap<String, Object> m = new HashMap<>();
        m.put("isAuthor", true);
        m.put("userId", "1");
        redisTemplate.opsForHash().putAll(sessionId, m);
        Object userId = redisTemplate.opsForHash().get(sessionId, "userId");
        if(userId == null)
            throw new ApiUnauthorizedException("Invalid session ID");
        return (String) userId;
    }
}
