package com.vox.post.repository;

import com.vox.post.model.Category;
import com.vox.post.model.Post;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

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

    @Aggregation(pipeline = {
            "{'$sort':  {'views' : -1}}",//Sort by views in descending order
            "{'$match': {'publishedAt':  {'$gte': ?1 }}}", //Filter by posts published in the last month
            "{'$group':  {'_id' : '$category', 'posts' : {'$push' : '$$ROOT'}}}", //Group by category and push all posts into an array,
            "{'$project': { category: \"$_id\", posts: { $slice: [\"$posts\", 0, ?0] } } }", //Slice the array to get the top posts
            "{'$unwind': '$posts'}", //Unwind the array
            "{'$replaceRoot': { 'newRoot': '$posts' } }" //Replace the root with the posts
    })
    List<Post> findTopPostsInEachCategory(Integer limit, LocalDateTime maxDate);

    @Query(value="{'id':'?0'}", fields="{'authorId' : 1}")
    String findPostByIdAndReturnAuthor(String postId);

}