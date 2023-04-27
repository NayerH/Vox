package com.vox.post.service.commands;

import com.vox.post.service.interfaces.ReturnIdCommand;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Component;

@Component
public class GetUserIdFromSession implements ReturnIdCommand {
    @Override
    public String execute(String sessionId) {
        //Implementation for Seif - Check sessionId if valid or not in cache then return userId
        //Return ApiUnauthorizedException("Invalid session ID")
        return null;
    }
}
