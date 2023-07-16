package com.example.demo.config;

import com.example.demo.redis.RedisService;
import com.example.demo.session.SessionRepository;
import com.example.demo.token.TokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final SessionRepository sessionRepository;
    private final RedisService redisService;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken =tokenRepository.findByToken(jwt)
                .orElse(null);
        var userId = storedToken.getUser().getId();

        if(storedToken!=null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
       Integer sessionID =  sessionRepository.findSessionIdByUserID(userId).getId();
//        ObjectMapper objectMapper = new ObjectMapper();
//        HashMap<String, Object> map = null;
//
//        try {
//            // Convert the JSON string back to a HashMap
//            map = objectMapper.readValue(sessionID, HashMap.class);
//        } catch (JsonProcessingException e) {
//            // Handle the exception
//        }
        redisService.removeSessionFromCache(sessionID);

    }

}
