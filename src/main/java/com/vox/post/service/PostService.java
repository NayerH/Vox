package com.vox.post.service;

import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    //Commands
    private ReturnManyCommand getAllPostsCommand;
    private ReturnOneCommand addPostCommand;
    private ReturnOneCommand getPostCommand;
    private ReturnOneCommand deletePostCommand;
    private CheckAuthorCommand checkIfAuthorizedCommand;
    private ReturnIdCommand getUserIdFromSession;
    private ReturnManyCommand getTopPostsInCategoryCommand;
    private ReturnManyCommand getTopPostsInCategoriesCommand;
    private UpdateCommand updatePostCommand;
    private CategoryWithSkipCommand getCategoryPostsCommand;


    @Autowired
    public PostService(ReturnManyCommand getAllPostsCommand, ReturnOneCommand addPostCommand, ReturnOneCommand getPostCommand, ReturnOneCommand deletePostCommand, CheckAuthorCommand checkIfAuthorizedCommand, ReturnIdCommand getUserIdFromSession, ReturnManyCommand getTopPostsInCategoryCommand, UpdateCommand updatePostCommand, CategoryWithSkipCommand getCategoryPostsCommand, ReturnManyCommand getTopPostsInCategoriesCommand){
        this.getAllPostsCommand = getAllPostsCommand;
        this.addPostCommand = addPostCommand;
        this.getPostCommand = getPostCommand;
        this.deletePostCommand = deletePostCommand;
        this.checkIfAuthorizedCommand = checkIfAuthorizedCommand;
        this.getUserIdFromSession = getUserIdFromSession;
        this.getTopPostsInCategoryCommand = getTopPostsInCategoryCommand;
        this.updatePostCommand = updatePostCommand;
        this.getCategoryPostsCommand = getCategoryPostsCommand;
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
        String userId = getUserIdFromSession.execute(sessionId);
        if(!checkIfAuthorizedCommand.execute(userId, postId)){
            throw new IllegalStateException("Only author is authorized to update the post");
        }
        return deletePostCommand.execute(postId);
    }
    public Post addPost(String sessionId, Post post){
        String userId = getUserIdFromSession.execute(sessionId);
        return addPostCommand.execute(post);
    }

    public Post updatePost(
            String sessionId,
            String postId,
            String title,
            String content,
            List<String> tags,
            Category.CategoryEnum category,
            List<Long> mediaFiles
    ) {
        String userId = getUserIdFromSession.execute(sessionId);
        if(!checkIfAuthorizedCommand.execute(userId, postId)){
            throw new IllegalStateException("Only author is authorized to update the post");
        }
        return updatePostCommand.execute(postId, title, content, tags, category, mediaFiles);
    }

//    Returns top posts in a category
    public List<Post> getTopPostsInCategory(Category.CategoryEnum categoryEnum){
        return getTopPostsInCategoryCommand.execute(categoryEnum);
    }

//    Return top posts in all categories
    public List<Post> getTopPostsInCategories() {
        return getTopPostsInCategoriesCommand.execute(null);
    }

    public List<Post> getCategoryPosts(Category.CategoryEnum category, Integer skip) {
        return getCategoryPostsCommand.execute(category, skip);
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
    public void setCheckIfAuthorizedCommand(CheckAuthorCommand checkIfAuthorizedCommand) {
        this.checkIfAuthorizedCommand = checkIfAuthorizedCommand;
    }
    @Autowired
    public void setGetUserIdFromSession(ReturnIdCommand getUserIdFromSession) {
        this.getUserIdFromSession = getUserIdFromSession;
    }
    @Autowired
    public void setGetTopPostsInCategoryCommand(ReturnManyCommand getTopPostsInCategoryCommand) {
        this.getTopPostsInCategoryCommand = getTopPostsInCategoryCommand;
    }

    @Autowired
    public void setGetTopPostsInCategoriesCommand(ReturnManyCommand getTopPostsInCategoriesCommand) {
        this.getTopPostsInCategoriesCommand = getTopPostsInCategoriesCommand;
    }

    @Autowired
    public void setUpdatePostCommand(UpdateCommand updatePostCommand) {
        this.updatePostCommand = updatePostCommand;
    }
    @Autowired
    public void setGetCategoryPostsCommand(CategoryWithSkipCommand getCategoryPostsCommand) {
        this.getCategoryPostsCommand = getCategoryPostsCommand;
    }


}
