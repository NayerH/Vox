package com.vox.post.model;

import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, MongoId> {
    @Aggregation(pipeline = {
            "{'$match' :  {'category' :  ?0}}",
            "{'$skip': ?1}",
            "{'$limit': 15}"
    })
    List<Post> findPostsByCategory(Category.CategoryEnum category, Integer skip);

    @Aggregation(pipeline = {
            "{'$match' :  {'category' :  ?0}}",
            "{'$sort':{'views':-1}}",
            "{'$limit': ?1}"
    })
    List<Post> findTopPostInCategory(Category.CategoryEnum category, Integer limit);

    @Query(value="{'id':'?0'}", fields="{'authorId' : 1}")
    String findPostByIdAndReturnAuthor(MongoId postId);

}