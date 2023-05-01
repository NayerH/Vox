package com.vox.post.controller.request;

import com.vox.post.model.Media;
import com.vox.post.model.MediaFile;
import com.vox.post.model.Post;

import java.util.List;

public class RequestWrapper {
    private Post post;
    private Media media;

    // getters and setters
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Media getMedia() {
        return media;
    }

    public void setMediaFiles(Media media) {
        this.media = media;
    }
}
