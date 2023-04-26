package com.vox.post.service;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.repository.PostRepository;
import com.vox.post.service.interfaces.CategoryWithSkipCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class GetCategoryPostsCommand implements CategoryWithSkipCommand {
    private final PostRepository postRepository;
    @Autowired
    public GetCategoryPostsCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> execute(Category.CategoryEnum category, Integer skip) {
        if(skip == null){
            skip = 0;
        }
        List<Post> posts = this.postRepository.findPostsByCategory(category, skip);
        return posts;
    }
}
