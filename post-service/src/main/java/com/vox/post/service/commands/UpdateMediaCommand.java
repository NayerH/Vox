package com.vox.post.service.commands;

import com.vox.post.exception.ApiRequestException;
import com.vox.post.model.Media;
import com.vox.post.model.MediaFile;
import com.vox.post.repository.media.MediaRepository;
import com.vox.post.service.interfaces.ReturnUpdatedMediaCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateMediaCommand implements ReturnUpdatedMediaCommand {
    private final MediaRepository mediaRepository;

    @Autowired
    public UpdateMediaCommand(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }
    @Override
    public Media execute(String mediaFilesReference, List<MediaFile> mediaFiles) {
        Media media = this.mediaRepository.findById(mediaFilesReference)
                .orElseThrow(() -> new ApiRequestException("Media file with id " + mediaFilesReference + " does not exist")
                );
        media.setMediaFiles(mediaFiles);
        this.mediaRepository.save(media);
        return media;
    }
}
