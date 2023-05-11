package com.vox.post.service;

import com.vox.post.exception.ApiUnauthorizedException;
import com.vox.post.model.*;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.commands.AddCommentCommand;
import com.vox.post.service.commands.AddMediaCommand;
import com.vox.post.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


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
    private IAddCommentCommand addCommentCommand;
    private IAddReplyCommand addReplyCommand;
    private ReturnManyMediaCommand addMediaCommand;
    private CheckIfUserIsAuthorCommand getIsAuthorFromSession;
    private ReturnUpdatedMediaCommand updateMediaCommand;

    @Autowired
    public PostService(ReturnManyCommand getAllPostsCommand,
                       ReturnOneCommand addPostCommand,
                       ReturnOneCommand getPostCommand,
                       ReturnOneCommand deletePostCommand,
                       CheckAuthorCommand checkIfAuthorizedCommand,
                       ReturnIdCommand getUserIdFromSession,
                       ReturnManyCommand getTopPostsInCategoryCommand,
                       UpdateCommand updatePostCommand,
                       CategoryWithSkipCommand getCategoryPostsCommand,
                       AddCommentCommand addCommentCommand,
                       IAddReplyCommand addReplyCommand,
                       ReturnManyMediaCommand addMediaCommand,
                       CheckIfUserIsAuthorCommand getIsAuthorFromSession,
                       ReturnUpdatedMediaCommand updateMediaCommand) {

        this.getAllPostsCommand = getAllPostsCommand;
        this.addPostCommand = addPostCommand;
        this.getPostCommand = getPostCommand;
        this.deletePostCommand = deletePostCommand;
        this.checkIfAuthorizedCommand = checkIfAuthorizedCommand;
        this.getUserIdFromSession = getUserIdFromSession;
        this.getTopPostsInCategoryCommand = getTopPostsInCategoryCommand;
        this.updatePostCommand = updatePostCommand;
        this.getCategoryPostsCommand = getCategoryPostsCommand;
        this.addCommentCommand = addCommentCommand;
        this.addReplyCommand = addReplyCommand;
        this.addMediaCommand = addMediaCommand;
        this.getIsAuthorFromSession = getIsAuthorFromSession;
        this.updateMediaCommand = updateMediaCommand;
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

    public void deletePost(String sessionId, String postId){
        String userId = getUserIdFromSession.execute(sessionId);
        if(!checkIfAuthorizedCommand.execute(userId, postId)){
            throw new ApiUnauthorizedException("Only author is authorized to update the post");
        }
        deletePostCommand.execute(postId);
    }
    public void addPost(String sessionId, Post post, Media media){
        Boolean isAuthor = getIsAuthorFromSession.execute(sessionId);
        if(!isAuthor){
            throw new ApiUnauthorizedException("Only author is authorized to add a new post");
        }
        String userId = getUserIdFromSession.execute(sessionId);
        if(media != null) {
            media.setUploaderId(userId);
            Media savedMedia = addMediaCommand.execute(media);
            String mediaReference = String.valueOf(savedMedia.getId());
            post.setMediaFilesRefrence(mediaReference);
        }
        post.setAuthorId(userId);
        addPostCommand.execute(post);
    }

    public void updatePost(
            String sessionId,
            String postId,
            String title,
            String content,
            List<String> tags,
            Category.CategoryEnum category,
            String mediaFilesReference,
            List<MediaFile> mediaFiles
    ) {
        String userId = getUserIdFromSession.execute(sessionId);
        if(!checkIfAuthorizedCommand.execute(userId, postId)){
            throw new ApiUnauthorizedException("Only author is authorized to update the post");
        }
        if (mediaFiles != null)
            updateMediaCommand.execute(mediaFilesReference,mediaFiles);
        updatePostCommand.execute(postId, title, content, tags, category);
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

    public void addComment(String sessionId, String postId, Comment comment) {
        String userId = getUserIdFromSession.execute(sessionId);
        if (comment == null || comment.getContent().length() == 0)
            throw new ApiUnauthorizedException("Comment cannot be empty");
        addCommentCommand.execute(userId,postId,comment);
    }

    public void addReply(String sessionId, String postId, String commentId, Comment reply) {
        String userId = getUserIdFromSession.execute(sessionId);
        if (reply == null || reply.getContent().length() == 0)
            throw new ApiUnauthorizedException("Reply cannot be empty");
        addReplyCommand.execute(userId,postId,commentId,reply);
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
    @Autowired
    public void setAddCommentCommand(IAddCommentCommand addCommentCommand) {
        this.addCommentCommand = addCommentCommand;
    }
    @Autowired
    public void setAddReplyCommand(IAddReplyCommand addReplyCommand) {
        this.addReplyCommand = addReplyCommand;
    }
    @Autowired
    public void setAddMediaCommand(ReturnManyMediaCommand addMediaCommand) {
        this.addMediaCommand = addMediaCommand;
    }
    @Autowired
    public void setGetIsAuthorFromSession(CheckIfUserIsAuthorCommand getIsAuthorFromSession) {
        this.getIsAuthorFromSession = getIsAuthorFromSession;
    }
}
