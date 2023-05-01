package com.vox.post.service.commands;

import com.vox.post.exception.ApiUnauthorizedException;
import com.vox.post.service.interfaces.ReturnIdCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetUserIdFromSession implements ReturnIdCommand {

    private CacheManager cacheManager;
    @Autowired
    public GetUserIdFromSession(@Qualifier("redisCloudCacheManager") CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    @Override
    public String execute(String sessionId) {
        //TODO: Remove Hardcoded Value
        HashMap<String, Object> m = new HashMap<>();
        m.put("isAuthor", true);
        m.put("userId", "1");
        cacheManager.getCache("sessions").put(sessionId, m);
        //END TODO
        Map<String, Object> sessionData = cacheManager.getCache("sessions").get(sessionId, HashMap.class);
        if(sessionData == null)
            throw new ApiUnauthorizedException("Invalid session ID");
        return sessionData.get("userId").toString();
    }
}
