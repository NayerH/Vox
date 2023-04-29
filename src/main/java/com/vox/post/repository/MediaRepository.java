package com.vox.post.repository;

import com.vox.post.model.Media;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaRepository extends MongoRepository<Media, String>{

}
