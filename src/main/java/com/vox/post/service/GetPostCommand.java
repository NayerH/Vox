package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.model.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetPostCommand implements ReturnOneCommand {
    private final PostRepository postRepository;
    @Autowired
    public GetPostCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    @Override
    public Post execute(Object o) {
        String m = (String) o;
        Optional<Post> optionalPost = postRepository.findById(m);
        if(optionalPost.isPresent()){
            Post p = optionalPost.get();
            p.setViews(p.getViews() + 1);
            this.postRepository.save(p);
            return p;
        }
        throw new IllegalStateException("No post is available with id " + m);
    }
}
