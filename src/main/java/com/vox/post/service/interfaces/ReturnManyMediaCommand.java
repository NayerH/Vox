package com.vox.post.service.interfaces;

import com.vox.post.model.Media;

public interface ReturnManyMediaCommand extends Command{
    Media execute(Object o);
}
