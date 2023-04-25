package com.vox.post.service.interfaces;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

public interface UpdateCommand {
    Post execute(MongoId postId,
                 String title,
                 String content,
                 List<String> tags,
                 Category.CategoryEnum category,
                 List<Long> mediaFiles);
}
