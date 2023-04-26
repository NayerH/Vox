package com.vox.post.service.commands;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.repository.PostRepository;
import com.vox.post.service.interfaces.ReturnManyCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GetTopPostsInCategoryCommand implements ReturnManyCommand {

    private PostRepository postRepository;
    @Autowired
    public GetTopPostsInCategoryCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> execute(Object o){
        Category.CategoryEnum c = (Category.CategoryEnum) o;
        Optional<List<Post>> optionalPost = Optional.ofNullable(postRepository.findTopPostInCategory(c, 3));
        if(optionalPost.isPresent()){
            return optionalPost.get();
        }
        throw new IllegalStateException("No post is available with category " + c);
    }

}
