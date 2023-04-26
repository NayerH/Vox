package com.vox.post.service;

import com.vox.post.model.MediaFile;
import com.vox.post.repository.MediaFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MediaFileService {

    private final MediaFileRepository mediaFileRepository;

    @Autowired
    public MediaFileService(MediaFileRepository mediaFileRepository) {
        this.mediaFileRepository = mediaFileRepository;
    }

    public Mono<MediaFile> save(MediaFile mediaFile) {
        return mediaFileRepository.save(mediaFile);
    }

    public Mono<MediaFile> findById(String id) {
        return mediaFileRepository.findById(id);
    }

    public Mono<Void> deleteById(String id) {
        return mediaFileRepository.deleteById(id);
    }

    public Flux<MediaFile> findAll() {
        return mediaFileRepository.findAll();
    }
}
