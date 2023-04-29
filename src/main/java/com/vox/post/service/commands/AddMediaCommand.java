package com.vox.post.service.commands;

import com.vox.post.model.Media;
import com.vox.post.repository.MediaRepository;
import com.vox.post.service.interfaces.ReturnManyMediaCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddMediaCommand implements ReturnManyMediaCommand {
    private final MediaRepository mediaRepository;

    @Autowired
    public AddMediaCommand(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Media execute(Object o) {
        Media m = (Media) o;
        return mediaRepository.save(m);
    }
}

