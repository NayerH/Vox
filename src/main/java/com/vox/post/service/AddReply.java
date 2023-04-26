package com.vox.post.service;

import com.vox.post.model.Comment;
import com.vox.post.model.Post;
import com.vox.post.model.PostRepository;
import com.vox.post.service.interfaces.AddReplyCommand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AddReply implements AddReplyCommand {

    private PostRepository postRepository;
    @Autowired
    public AddReply(PostRepository postRepository) {
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
