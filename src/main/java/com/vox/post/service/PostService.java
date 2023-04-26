package com.vox.post.service;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.service.interfaces.AuthenticationCommand;
import com.vox.post.service.interfaces.ReturnIdCommand;
import com.vox.post.service.interfaces.ReturnManyCommand;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings({"rawtypes", "deprecation"})
@Service
public class PostService {
    //Commands
    private ReturnManyCommand getAllPostsCommand;
    private final ReturnOneCommand addPostCommand;
    private ReturnOneCommand getPostCommand;
    private ReturnOneCommand deletePostCommand;
    private AuthenticationCommand checkIfAuthorCommand;
    private ReturnIdCommand getUserIdFromSession;
    private final ReturnManyCommand getTopPostsInCategoryCommand;
    private final ReturnManyCommand getTopPostsInCategoriesCommand;

    @Autowired
    public PostService(ReturnManyCommand getAllPostsCommand, ReturnOneCommand addPostCommand, ReturnOneCommand getPostCommand, ReturnOneCommand deletePostCommand, AuthenticationCommand checkIfAuthorCommand, ReturnIdCommand getUserIdFromSession, ReturnManyCommand getTopPostsInCategoryCommand, ReturnManyCommand getTopPostsInCategoriesCommand) {
        this.getAllPostsCommand = getAllPostsCommand;
        this.addPostCommand = addPostCommand;
        this.getPostCommand = getPostCommand;
        this.deletePostCommand = deletePostCommand;
        this.checkIfAuthorCommand = checkIfAuthorCommand;
        this.getUserIdFromSession = getUserIdFromSession;
        this.getTopPostsInCategoryCommand = getTopPostsInCategoryCommand;
        this.getTopPostsInCategoriesCommand = getTopPostsInCategoriesCommand;
    }

    //Functionalities
//    Get all posts
    public List<Post> getPosts(){
        return getAllPostsCommand.execute(null);
    }
//    Get post by id
    public Post getPost(String id){
        return getPostCommand.execute(id);
    }
    public Post deletePost(String sessionId, String postId){
//        TODO: Uncomment this when authentication is implemented
//        MongoId userId = getUserIdFromSession.execute(sessionId);
//        if(!checkIfAuthorCommand.execute(userId)){
//            throw new IllegalStateException("Invalid session ID");
//        }
        return deletePostCommand.execute(postId);
    }
    public Post addPost(String sessionId, Post post){
//        TODO: Uncomment this when authentication is implemented
//        MongoId userId = getUserIdFromSession.execute(sessionId);
//        if(!checkIfAuthorCommand.execute(userId)){
//            throw new IllegalStateException("Invalid session ID");
//        }
        return addPostCommand.execute(post);
    }

//    TODO: Implement Update Functionality
    public Post updatePost(String id, String id1, Post post) {
        return null;
    }

//    Returns top posts in a category
    public List<Post> getTopPostsInCategory(Category.CategoryEnum categoryEnum){
        return getTopPostsInCategoryCommand.execute(categoryEnum);
    }

//    Return top posts in all categories
    public List<Post> getTopPostsInCategories() {
        return getTopPostsInCategoriesCommand.execute(null);
    }

}
