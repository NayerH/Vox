package com.vox.post.service.commands;

import com.vox.post.model.Post;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

@Component
public class AddPostCommand implements ReturnOneCommand {

    private final PostRepository postRepository;
    private final RedisCacheManager redisCacheManager;
    @Autowired
    public AddPostCommand(PostRepository postRepository, @Qualifier("redisCacheManager") RedisCacheManager redisCacheManager) {
        this.postRepository = postRepository;
        this.redisCacheManager = redisCacheManager;
    }

    @Override
    public Post execute(Object o) {
        Post p = (Post) o;
        Post savedPost =  postRepository.save(p);
        redisCacheManager.getCache("posts").put(savedPost.getId(), savedPost);
        return savedPost;
    }
}
