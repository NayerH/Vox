package com.vox.post.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class MediaFileDocument {
    @Id
    private Long id;
    private List<MediaFile> mediaFileList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MediaFile> getMediaFileList() {
        return mediaFileList;
    }

    public void setMediaFileList(List<MediaFile> mediaFileList) {
        this.mediaFileList = mediaFileList;
    }

    @Override
    public String toString() {
        return "MediaFileDocument{" +
                "id=" + id +
                ", mediaFileList=" + mediaFileList +
                '}';
    }
}
