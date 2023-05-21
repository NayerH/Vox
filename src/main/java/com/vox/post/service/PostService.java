package com.vox.post.service;

import com.vox.post.exception.ApiRequestException;
import com.vox.post.exception.ApiUnauthorizedException;
import com.vox.post.model.*;
import com.vox.post.service.commands.AddCommentCommand;
import com.vox.post.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Map;

@Service
public class PostService {
    private final Map<String, Command> commands;

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
                       ReturnUpdatedMediaCommand updateMediaCommand,
                       ReturnManyCommand getTopPostsInCategoriesCommand,
                       Map<String, Command> commands) {
        this.commands = commands;
        commands.put("getAllPostsCommand", getAllPostsCommand);
        commands.put("addPostCommand", addPostCommand);
        commands.put("getPostCommand", getPostCommand);
        commands.put("deletePostCommand", deletePostCommand);
        commands.put("checkIfAuthorizedCommand", checkIfAuthorizedCommand);
        commands.put("getUserIdFromSession", getUserIdFromSession);
        commands.put("getTopPostsInCategoryCommand", getTopPostsInCategoryCommand);
        commands.put("updatePostCommand", updatePostCommand);
        commands.put("getCategoryPostsCommand", getCategoryPostsCommand);
        commands.put("addCommentCommand", addCommentCommand);
        commands.put("addReplyCommand", addReplyCommand);
        commands.put("addMediaCommand", addMediaCommand);
        commands.put("getIsAuthorFromSession", getIsAuthorFromSession);
        commands.put("updateMediaCommand", updateMediaCommand);
        commands.put("getTopPostsInCategoriesCommand", getTopPostsInCategoriesCommand);
    }

    //Functionalities
//    Get all posts
    public List<Post> getPosts(){
        return ((ReturnManyCommand) commands.get("getAllPostsCommand")).execute(null);
    }
//    Get post by id
    public Post getPost(String id){
        return ((ReturnOneCommand) commands.get("getPostCommand")).execute(id);
    }

    public void deletePost(String sessionId, String postId){
        String userId = ((ReturnIdCommand) commands.get("getUserIdFromSession")).execute(sessionId);
        if(!((CheckAuthorCommand)commands.get("checkIfAuthorizedCommand")).execute(userId, postId)){
            throw new ApiUnauthorizedException("Only author is authorized to update the post");
        }
        ((ReturnOneCommand) commands.get("deletePostCommand")).execute(postId);
    }
    public void addPost(String sessionId, Post post, Media media){
        Boolean isAuthor = ((CheckIfUserIsAuthorCommand) commands.get("getIsAuthorFromSession")).execute(sessionId);
        if(!isAuthor){
            throw new ApiUnauthorizedException("Only author is authorized to add a new post");
        }
        String userId = ((ReturnIdCommand) commands.get("getUserIdFromSession")).execute(sessionId);
        if(media != null) {
            media.setUploaderId(userId);
            Media savedMedia = ((ReturnManyMediaCommand) commands.get("addMediaCommand")).execute(media);
            String mediaReference = String.valueOf(savedMedia.getId());
            post.setMediaFilesRefrence(mediaReference);
        }
        post.setAuthorId(userId);
        ((ReturnOneCommand) commands.get("addPostCommand")).execute(post);
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
        String userId = ((ReturnIdCommand) commands.get("getUserIdFromSession")).execute(sessionId);
        if(!((CheckAuthorCommand)commands.get("checkIfAuthorizedCommand")).execute(userId, postId)){
            throw new ApiUnauthorizedException("Only author is authorized to update the post");
        }
        if (mediaFiles != null)
            ((ReturnUpdatedMediaCommand) commands.get("updateMediaCommand")).execute(mediaFilesReference,mediaFiles);
        ((UpdateCommand) commands.get("updatePostCommand")).execute(postId, title, content, tags, category);
    }

//    Returns top posts in a category
    public List<Post> getTopPostsInCategory(Category.CategoryEnum categoryEnum){
        return ((ReturnManyCommand) commands.get("getTopPostsInCategoryCommand")).execute(categoryEnum);
    }

//    Return top posts in all categories
    public List<Post> getTopPostsInCategories() {
        return ((ReturnManyCommand) commands.get("getTopPostsInCategoriesCommand")).execute(null);
    }

    public List<Post> getCategoryPosts(Category.CategoryEnum category, Integer skip) {
        return ((CategoryWithSkipCommand) commands.get("getCategoryPostsCommand")).execute(category, skip);
    }

    public void addComment(String sessionId, String postId, Comment comment) {
        String userId = ((ReturnIdCommand) commands.get("getUserIdFromSession")).execute(sessionId);
        if (comment == null || comment.getContent().length() == 0)
            throw new ApiUnauthorizedException("Comment cannot be empty");
        ((AddCommentCommand) commands.get("addCommentCommand")).execute(userId,postId,comment);
    }

    public void addReply(String sessionId, String postId, String commentId, Comment reply) {
        String userId = ((ReturnIdCommand) commands.get("getUserIdFromSession")).execute(sessionId);
        if (reply == null || reply.getContent().length() == 0)
            throw new ApiUnauthorizedException("Reply cannot be empty");
        ((IAddReplyCommand) commands.get("addReplyCommand")).execute(userId,postId,commentId,reply);
    }

    public void addCommand(String key, Command command){
        if(commands.containsKey(key)){
            throw new ApiRequestException("Command already exists");
        }
        commands.put(key, command);
    }

    public void removeCommand(String key){
        if (!commands.containsKey(key)){
            throw new ApiRequestException("Command does not exist");
        }
        commands.remove(key);
    }

    public void updateCommand(String key, Command command){
        if (!commands.containsKey(key)){
            throw new ApiRequestException("Command does not exist");
        }
        commands.replace(key, command);
    }
}
