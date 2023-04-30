package com.vox.post.service.commands;

import com.vox.post.exception.ApiUnauthorizedException;
import com.vox.post.service.interfaces.CheckIfUserIsAuthorCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class GetIsAuthorFromSession implements CheckIfUserIsAuthorCommand {
    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired
    public GetIsAuthorFromSession(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Boolean execute(String sessionId) {
        //TODO: Remove Hardcoded Value
        Object isAuthor = redisTemplate.opsForHash().get("C1E11A4E06310EBBDF1B864528C6F1A8", "isAuthor");
        if(isAuthor == null)
            throw new ApiUnauthorizedException("Invalid session ID");
        return (Boolean) isAuthor;
    }
}
