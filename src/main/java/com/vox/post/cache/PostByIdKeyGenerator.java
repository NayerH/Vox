package com.vox.post.cache;

import com.vox.post.model.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.time.LocalDate;

public class PostByIdKeyGenerator implements KeyGenerator {

    private final LocalDate date = LocalDate.now().minus(1, java.time.temporal.ChronoUnit.MONTHS);


    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (method.getName().equals("getPost")) {
            Post post = (Post) params[0];
            if (post.getPublishedAt().isAfter(date) && post.getViews() >= 1000000) {
                return post.getId();
            }
        }
        return null;
    }
}
