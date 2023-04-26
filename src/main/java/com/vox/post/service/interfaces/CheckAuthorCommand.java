package com.vox.post.service.interfaces;

public interface CheckAuthorCommand extends Command {
     Boolean execute(String userId, String postId);
}
