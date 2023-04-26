package com.vox.post.service;

import com.vox.post.model.Comment;
import com.vox.post.model.Post;
import com.vox.post.model.PostRepository;
import com.vox.post.service.interfaces.AddCommentCommand;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;

@Service
public class AddComment implements AddCommentCommand {

    private PostRepository postRepository;

    @Autowired
    public AddComment(PostRepository postRepository) {
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