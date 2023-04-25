package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.model.PostRepository;
import com.vox.post.service.interfaces.ReturnManyCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GetTopPostsInCategoriesCommand implements ReturnManyCommand {

    @Autowired
    private PostRepository postRepository;


    @Override
    public List<Post> execute(Object o){
        Integer limit = (Integer) o;
        Optional<List<Post>> optionalPost = postRepository.findTopPostsInEachCategory(limit);
        if(optionalPost.isPresent()){
            return optionalPost.get();
        }
        throw new IllegalStateException("No posts are available");
    }

}
