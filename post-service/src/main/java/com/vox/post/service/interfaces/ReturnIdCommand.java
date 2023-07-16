package com.vox.post.service.interfaces;

import org.springframework.data.mongodb.core.mapping.MongoId;

public interface ReturnIdCommand extends Command {
    String execute(String sessionId);
}
