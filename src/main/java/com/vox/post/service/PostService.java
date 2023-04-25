package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.service.interfaces.AuthenticationCommand;
import com.vox.post.service.interfaces.ReturnIdCommand;
import com.vox.post.service.interfaces.ReturnManyCommand;
import com.vox.post.service.interfaces.ReturnOneCommand;
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

    public PostService() {
        this.setGetAllPostsCommand();
        this.setAddPostCommand();
        this.setGetPostCommand();
        this.setDeletePostCommand();
        this.setCheckIfAuthorCommand();
        this.setGetUserIdFromSession();
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

    //Setters
    public void setGetAllPostsCommand(){
        Class c;
        try {
            c = Class.forName("com.vox.post.service.GetAllPostsCommand");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            getAllPostsCommand = (ReturnManyCommand) c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void setGetPostCommand(){
        Class c;
        try {
            c = Class.forName("com.vox.post.service.GetPostCommand");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            getPostCommand = (ReturnOneCommand) c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void setDeletePostCommand(){
        Class c;
        try {
            c = Class.forName("com.vox.post.service.DeletePostCommand");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            deletePostCommand = (ReturnOneCommand) c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void setAddPostCommand(){
        Class c;
        try {
            c = Class.forName("com.vox.post.service.AddPostCommand");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            addPostCommand = (ReturnOneCommand) c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void setCheckIfAuthorCommand() {
        Class c;
        try {
            c = Class.forName("com.vox.post.service.CheckIfAuthorCommand");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            checkIfAuthorCommand = (AuthenticationCommand) c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public void setGetUserIdFromSession() {
        Class c;
        try {
            c = Class.forName("com.vox.post.service.GetUserIdFromSession");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            getUserIdFromSession = (ReturnIdCommand) c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
