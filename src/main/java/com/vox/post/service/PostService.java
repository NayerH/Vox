package com.vox.post.service;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.service.interfaces.AuthenticationCommand;
import com.vox.post.service.interfaces.ReturnIdCommand;
import com.vox.post.service.interfaces.ReturnManyCommand;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings({"rawtypes", "deprecation"})
@Service
public class PostService {
    //Commands
    private ReturnManyCommand getAllPostsCommand;
    private ReturnOneCommand addPostCommand;
    private ReturnOneCommand getPostCommand;
    private ReturnOneCommand deletePostCommand;
    private AuthenticationCommand checkIfAuthorCommand;
    private ReturnIdCommand getUserIdFromSession;
    private ReturnManyCommand getTopPostsInCategoryCommand;

    @Autowired
    public PostService(ReturnManyCommand getAllPostsCommand, ReturnOneCommand addPostCommand, ReturnOneCommand getPostCommand, ReturnOneCommand deletePostCommand, AuthenticationCommand checkIfAuthorCommand, ReturnIdCommand getUserIdFromSession) {
        this.getAllPostsCommand = getAllPostsCommand;
        this.addPostCommand = addPostCommand;
        this.getPostCommand = getPostCommand;
        this.deletePostCommand = deletePostCommand;
        this.checkIfAuthorCommand = checkIfAuthorCommand;
        this.getUserIdFromSession = getUserIdFromSession;
    }

    //Functionalities
    public List<Post> getPosts(){
        return getAllPostsCommand.execute(null);
    }
    public Post getPost(MongoId id){
        return getPostCommand.execute(id);
    }
    public Post deletePost(String sessionId, MongoId postId){
        MongoId userId = getUserIdFromSession.execute(sessionId);
        if(!checkIfAuthorCommand.execute(userId)){
            throw new IllegalStateException("Invalid session ID");
        }
        return deletePostCommand.execute(postId);
    }
    public Post addPost(String sessionId, Post post){
        MongoId userId = getUserIdFromSession.execute(sessionId);
        if(!checkIfAuthorCommand.execute(userId)){
            throw new IllegalStateException("Invalid session ID");
        }
        return addPostCommand.execute(post);
    }

    public Post updatePost(String id, MongoId id1, Post post) {
        return null;
    }

    public List<Post> getTopPostsInCategory(Category.CategoryEnum categoryEnum){
        return getTopPostsInCategoryCommand.execute(categoryEnum);
    }

    //Setters
    @Autowired
    public void setGetAllPostsCommand(ReturnManyCommand getAllPostsCommand) {
        this.getAllPostsCommand = getAllPostsCommand;
    }
    @Autowired
    public void setAddPostCommand(ReturnOneCommand addPostCommand) {
        this.addPostCommand = addPostCommand;
    }
    @Autowired
    public void setGetPostCommand(ReturnOneCommand getPostCommand) {
        this.getPostCommand = getPostCommand;
    }
    @Autowired
    public void setDeletePostCommand(ReturnOneCommand deletePostCommand) {
        this.deletePostCommand = deletePostCommand;
    }
    @Autowired
    public void setCheckIfAuthorCommand(AuthenticationCommand checkIfAuthorCommand) {
        this.checkIfAuthorCommand = checkIfAuthorCommand;
    }
    @Autowired
    public void setGetUserIdFromSession(ReturnIdCommand getUserIdFromSession) {
        this.getUserIdFromSession = getUserIdFromSession;
    }
    @Autowired
    public void setGetTopPostsInCategoryCommand(ReturnManyCommand getTopPostsInCategoryCommand) {
        this.getTopPostsInCategoryCommand = getTopPostsInCategoryCommand;
    }
}
