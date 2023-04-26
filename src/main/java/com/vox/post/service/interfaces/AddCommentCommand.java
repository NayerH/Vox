package com.vox.post.service.interfaces;

import org.springframework.data.mongodb.core.mapping.MongoId;

public interface AddCommentCommand extends Command{

    void execute(MongoId postId, String comment);
}
