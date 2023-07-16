package com.example.demo.redis;

import com.example.demo.session.Session;
import com.example.demo.session.SessionRepository;
import com.example.demo.token.Token;
import com.example.demo.user.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisService {
    @Autowired
    @Qualifier("redisCloudCacheManager")
    private CacheManager cacheManager;
    @Autowired
    private SessionRepository sessionRepository;

    @Cacheable(cacheNames = "sessions", key = "#token.getId()")
    public Session createSession(Token token){
        Users user = token.getUser();
        Session session = new Session();
        session.setToken(token.getToken());
        session.setRole(user.getRole());
        session.setUserID(user.getId());
        return session;
    }


    @CacheEvict(cacheNames = "sessions", key = "#token.getId()")
    public void deleteSession(Token token){
    }
    public Session execute(Token token) {
        //TODO: Remove Hardcoded Value
    System.out.println("execute");
      Users user = token.getUser();
        Session session = new Session();
        session.setToken(token.getToken());
        session.setRole(user.getRole());
        session.setUserID(user.getId());


        HashMap<String, Object> m = new HashMap<>();
       // session.setSessionID(m.toString());



        if(user.getRole().equals("AUTHOR")){
            m.put("isAuthor", true);

        }else{
            m.put("isAuthor", false);

        }

        m.put("userId",user.getId());
        session= sessionRepository.save(session);
        System.out.println(session);
        System.out.println("m"+ m);

        cacheManager.getCache("sessions").put(session.getId(),m);
//        String sessionID = null;
//
//        try {
//            sessionID = new ObjectMapper().writeValueAsString(m);
//        } catch (JsonProcessingException e) {
//            // Handle the exception
//        }
//        session.setSessionID(sessionID);

        //END TODO
       return session;
    }

    public void removeSessionFromCache(Integer sessionId) {

        cacheManager.getCache("sessions").evict(sessionId);
    }


}