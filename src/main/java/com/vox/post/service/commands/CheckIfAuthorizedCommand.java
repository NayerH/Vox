package com.vox.post.service.commands;

import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.CheckAuthorCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckIfAuthorizedCommand implements CheckAuthorCommand {

    private final PostRepository postRepository;
    @Autowired
    public CheckIfAuthorizedCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Boolean execute(String userId, String authorId) {
        String postAuthorId = this.postRepository.findPostByIdAndReturnAuthor(userId);
        return authorId.equals(postAuthorId);
    }
}
