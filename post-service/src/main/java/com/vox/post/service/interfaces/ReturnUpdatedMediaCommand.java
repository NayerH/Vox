package com.vox.post.service.interfaces;

import com.vox.post.model.Media;
import com.vox.post.model.MediaFile;

import java.util.List;

public interface ReturnUpdatedMediaCommand extends Command{

    Media execute(String mediaFilesReference, List<MediaFile> mediaFiles);
}
