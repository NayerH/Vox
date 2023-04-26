package com.vox.post.service.commands;

import com.vox.post.model.Comment;
import com.vox.post.model.Post;
import com.vox.post.repository.PostRepository;
import com.vox.post.service.interfaces.IAddReplyCommand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AddReplyCommand implements IAddReplyCommand {

    private PostRepository postRepository;
    @Autowired
    public AddReplyCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public void execute(String userId, String postId, String commentId, Comment reply) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("Post with Id: " + postId + " does not exist"));
        reply.setUserId(userId);
        post.addReply(commentId, reply);
        this.postRepository.save(post);
    }
}
