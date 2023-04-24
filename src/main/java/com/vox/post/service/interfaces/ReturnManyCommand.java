package com.vox.post.service.interfaces;

import com.vox.post.model.Post;

import java.util.List;

public interface ReturnManyCommand extends Command {
    public List<Post> execute(Object o);

}
