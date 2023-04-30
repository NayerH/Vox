package com.vox.post.service.commands;

import com.vox.post.exception.ApiRequestException;
import com.vox.post.model.Comment;
import com.vox.post.model.Post;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.IAddReplyCommand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AddReplyCommand implements IAddReplyCommand {

    private final PostRepository postRepository;
    @Autowired
    public AddReplyCommand(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public void execute(String userId, String postId, String commentId, Comment reply) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiRequestException("Post with Id: " + postId + " does not exist"));
        reply.setUserId(userId);
        Boolean commentFound = false;
        for (Comment comment : post.getComments()) {
            if (comment.getId().equals(commentId)){
                comment.addReply(reply);
                commentFound = true;
                break;
            }
        }
        if(!commentFound) {
            throw new ApiRequestException("Comment with id: " + commentId + " not found");
        }
        this.postRepository.save(post);
    }
}
