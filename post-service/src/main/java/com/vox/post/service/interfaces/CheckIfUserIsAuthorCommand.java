package com.vox.post.service.interfaces;

public interface CheckIfUserIsAuthorCommand extends Command {
    Boolean execute(String sessionId);
}