package com.vox.post.service;

import com.vox.post.model.Post;
import com.vox.post.service.interfaces.ReturnManyCommand;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    //Commands
    private ReturnManyCommand getAllPostsCommand;
    private ReturnOneCommand addPostCommand;
    private ReturnOneCommand getPostCommand;
    private ReturnOneCommand deletePostCommand;

    public PostService() {
        this.setGetAllPostsCommand();
        this.setAddPostCommand();
        this.setGetPostCommand();
        this.setDeletePostCommand();
    }

    //Functionalities
    public List<Post> getPosts(){
        return getAllPostsCommand.execute(null);
    }
    public Post getPost(MongoId id){
        return getPostCommand.execute(id);
    }
    public Post deletePost(MongoId id){
        return deletePostCommand.execute(id);
    }
    public Post addPost(Post post){
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
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
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
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
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
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
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
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
