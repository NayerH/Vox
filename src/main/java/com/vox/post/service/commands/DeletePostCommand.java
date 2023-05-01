package com.vox.post.service.commands;

import com.vox.post.exception.ApiRequestException;
import com.vox.post.model.Post;
import com.vox.post.repository.media.MediaRepository;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeletePostCommand implements ReturnOneCommand {
    private final PostRepository postRepository;
    private final MediaRepository mediaRepository;

    @Autowired
    public DeletePostCommand(PostRepository postRepository, MediaRepository mediaRepository) {
        this.postRepository = postRepository;
        this.mediaRepository = mediaRepository;
    }
    @Override
    public Post execute(Object o) {
        String m = (String) o;
        Post post = this.postRepository.findById(m)
                .orElseThrow(() -> new ApiRequestException("No post is available with id " + m + "to be deleted")
                );
        String mediaFilesRefrence = post.getMediaFilesRefrence();
        mediaRepository.deleteById(mediaFilesRefrence);
        postRepository.deleteById(m);
        return null;
    }
}
