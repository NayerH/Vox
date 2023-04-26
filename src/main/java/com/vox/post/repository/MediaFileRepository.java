package com.vox.post.repository;

import com.vox.post.model.MediaFile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaFileRepository extends ReactiveCrudRepository<MediaFile, String> {

}
