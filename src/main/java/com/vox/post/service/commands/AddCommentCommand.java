package com.vox.post.service.commands;

import com.vox.post.model.Comment;
import com.vox.post.model.Post;
import com.vox.post.repository.PostRepository;
import com.vox.post.service.interfaces.IAddCommentCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AddCommentCommand implements IAddCommentCommand {

    private PostRepository postRepository;

    @Autowired
    public AddCommentCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void execute(String userId, String postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("Post with Id: " + postId + " does not exist"));
        comment.setUserId(userId);
        post.addComment(comment);
        this.postRepository.save(post);
    }
}