package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.model.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeletePostCommand implements ReturnOneCommand {

    private PostRepository postRepository;
    @Autowired
    public DeletePostCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post execute(Object o) {
        MongoId m = (MongoId) o;
        boolean exists = postRepository.existsById(m);
        if(!exists){
            throw new IllegalStateException("No post is available with id " + m + "to be deleted");
        }
        postRepository.findById(m).get();
        return null;
    }
}
