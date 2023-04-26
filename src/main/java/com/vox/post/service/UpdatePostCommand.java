package com.vox.post.service;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.model.PostRepository;
import com.vox.post.service.interfaces.UpdateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdatePostCommand implements UpdateCommand {
    private final PostRepository postRepository;
    @Autowired
    public UpdatePostCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post execute(String postId,
                        String title,
                        String content,
                        List<String> tags,
                        Category.CategoryEnum category,
                        List<Long> mediaFiles)
    {
        Post post = this.postRepository.findById(postId).orElseThrow(
                () -> new IllegalStateException("Post with id " + postId + " does not exist")
        );
        if (checkValidString(title, post.getTitle())){
            post.setTitle(title);
        }
        if (checkValidString(content, post.getContent())){
            post.setContent(content);
        }
        if(tags != null){
            post.setTags(tags);
        }
        if(mediaFiles != null){
            post.setMediaFiles(mediaFiles);
        }
        if(category != null){
            post.setCategory(category);
        }
        this.postRepository.save(post);
        return post;
    }

    private Boolean checkValidString(String o1, String o2) {
        if (o1 == null) return false;
        if (o1.length() == 0) return false;
        return !o1.equals(o2);
    }
}
