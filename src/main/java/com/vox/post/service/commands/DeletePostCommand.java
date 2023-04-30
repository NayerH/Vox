package com.vox.post.service.commands;

import com.vox.post.exception.ApiRequestException;
import com.vox.post.model.Post;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeletePostCommand implements ReturnOneCommand {
    private final PostRepository postRepository;
    @Autowired
    public DeletePostCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    @Override
    public Post execute(Object o) {
        String m = (String) o;
        boolean exists = postRepository.existsById(m);
        if(!exists){
            throw new ApiRequestException("No post is available with id " + m + "to be deleted");
        }
        postRepository.deleteById(m);
        return null;
    }
}
