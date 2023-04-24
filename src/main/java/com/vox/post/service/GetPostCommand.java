package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.model.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetPostCommand implements ReturnOneCommand {

    private PostRepository postRepository;
    @Autowired
    public GetPostCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post execute(Object o) {
        MongoId m = (MongoId) o;
        Optional<Post> optionalPost = postRepository.findById(m);
        if(optionalPost.isPresent()){
            return optionalPost.get();
        }
        throw new IllegalStateException("No post is available with id " + m);
    }
}
