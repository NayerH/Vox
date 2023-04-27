package com.vox.post.service.interfaces;

import com.vox.post.model.Comment;

public interface IAddCommentCommand extends Command{

    void execute(String userId, String postId, Comment comment);
}
