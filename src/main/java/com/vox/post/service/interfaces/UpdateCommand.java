package com.vox.post.service.interfaces;

import com.vox.post.model.Category;
import com.vox.post.model.Post;

import java.util.List;

public interface UpdateCommand {
    Post execute(String postId,
                 String title,
                 String content,
                 List<String> tags,
                 Category.CategoryEnum category);
}
