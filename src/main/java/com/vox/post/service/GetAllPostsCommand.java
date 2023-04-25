package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.model.PostRepository;
import com.vox.post.service.interfaces.ReturnManyCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllPostsCommand implements ReturnManyCommand {
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> execute(Object o) {
        return postRepository.findAll();
    }
}
