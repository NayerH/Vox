package com.vox.post.service.commands;

import com.vox.post.exception.ApiUnauthorizedException;
import com.vox.post.service.interfaces.CheckIfUserIsAuthorCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GetIsAuthorFromSession implements CheckIfUserIsAuthorCommand {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public GetIsAuthorFromSession(@Qualifier("redisUserTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Boolean execute(String sessionId) {
        //TODO: Remove Hardcoded Value
        HashMap<String, Object> m = new HashMap<>();
        m.put("isAuthor", true);
        m.put("userId", "1");
        redisTemplate.opsForHash().putAll(sessionId, m);
        Object isAuthor = redisTemplate.opsForHash().get(sessionId, "isAuthor");
        if(isAuthor == null)
            throw new ApiUnauthorizedException("Invalid session ID");
        return (Boolean) isAuthor;
    }
}
