package com.vox.post.controller.request;

import com.vox.post.model.Category;
import com.vox.post.model.MediaFile;

import java.util.List;

public class UpdateWrapper {
    private String title;
    private String content;
    private List<String> tags;
    private Category.CategoryEnum category;
    private String mediaFilesReference;
    private List<MediaFile> mediaFiles;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "UpdateWrapper{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tags=" + tags +
                ", category=" + category +
                ", mediaFilesReference='" + mediaFilesReference + '\'' +
                ", mediaFiles=" + mediaFiles +
                '}';
    }

    public List<String> getTags() {
        return tags;
    }

    public Category.CategoryEnum getCategory() {
        return category;
    }

    public String getMediaFilesReference() {
        return mediaFilesReference;
    }

    public List<MediaFile> getMediaFiles() {
        return mediaFiles;
    }
}
