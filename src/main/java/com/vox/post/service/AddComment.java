package com.vox.post.service;

import com.vox.post.service.interfaces.AddCommentCommand;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

@Component
public class AddComment implements AddCommentCommand {

    @Override
    public void execute(MongoId postId, String comment) {

    }
}
