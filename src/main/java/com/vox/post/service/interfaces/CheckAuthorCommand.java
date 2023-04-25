package com.vox.post.service.interfaces;

import org.springframework.data.mongodb.core.mapping.MongoId;

public interface CheckAuthorCommand extends Command {
     Boolean execute(MongoId userId, MongoId postId);
}
