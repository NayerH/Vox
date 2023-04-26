package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.repository.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddPostCommand implements ReturnOneCommand {

    private PostRepository postRepository;
    @Autowired
    public AddPostCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post execute(Object o) {
        Post p = (Post) o;
        return postRepository.save(p);
    }
}
