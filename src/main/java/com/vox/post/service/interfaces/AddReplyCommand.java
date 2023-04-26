package com.vox.post.service.interfaces;

import com.vox.post.model.Comment;
import org.springframework.data.mongodb.core.mapping.MongoId;

public interface AddReplyCommand extends Command{

    void execute(String userId, String postId, String commentId, Comment reply);
}
