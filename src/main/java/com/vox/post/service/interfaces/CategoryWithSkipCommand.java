package com.vox.post.service.interfaces;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import java.util.List;

public interface CategoryWithSkipCommand extends Command{

    List<Post> execute(Category.CategoryEnum category, Integer skip);
}
