package com.vox.post.service.interfaces;

import com.vox.post.model.Post;

public interface ReturnOneCommand extends Command {
    public Post execute(Object o);
}
