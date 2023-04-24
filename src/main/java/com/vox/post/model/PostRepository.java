package com.vox.post.model;

import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, MongoId> {


    List<Post> findPostsByCategory(Category.CategoryEnum category);

    @Aggregation(pipeline = {
            "{'$match' :  {'category' :  ?0}}",
            "{'$sort':{'views':-1}}",
            "{'$limit': ?1}"
    })
    List<Post> findTopPostInCategory(Category.CategoryEnum category, Integer limit);

//    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
//    List<GroceryItem> findAll(String category);

}