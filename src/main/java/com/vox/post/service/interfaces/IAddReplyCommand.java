package com.vox.post.service.interfaces;

import com.vox.post.model.Comment;

public interface IAddReplyCommand extends Command{

    void execute(String userId, String postId, String commentId, Comment reply);
}
