package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.repository.PostRepository;
import com.vox.post.service.interfaces.ReturnManyCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllPostsCommand implements ReturnManyCommand {
    private PostRepository postRepository;
    @Autowired
    public GetAllPostsCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    @Override
    public List<Post> execute(Object o) {
        return postRepository.findAll();
    }
}
