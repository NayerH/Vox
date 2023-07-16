package com.vox.post.service;

import com.vox.post.exception.ApiUnauthorizedException;
import com.vox.post.service.interfaces.CheckIfUserIsAuthorCommand;
import com.vox.post.service.interfaces.IAddMediaFileCommand;
import com.vox.post.service.interfaces.ReturnIdCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Service

public class MediaService {

    private ReturnIdCommand getUserIdFromSession;
    private IAddMediaFileCommand addMediaFileCommand;
    private CheckIfUserIsAuthorCommand getIsAuthorFromSession;

    @Autowired
    public MediaService (ReturnIdCommand getUserIdFromSession,
                         IAddMediaFileCommand addMediaFileCommand,
                         CheckIfUserIsAuthorCommand getIsAuthorFromSession) {

        this.getUserIdFromSession = getUserIdFromSession;
        this.getIsAuthorFromSession = getIsAuthorFromSession;
        this.addMediaFileCommand = addMediaFileCommand;
    }

    public ArrayList<String> addMedia(String sessionId, ArrayList<MultipartFile> files) throws IOException {
//        getUserIdFromSession.execute(sessionId);
//        Boolean isAuthor = getIsAuthorFromSession.execute(sessionId);
//        if(!isAuthor){
//            throw new ApiUnauthorizedException("Only author is authorized to add a new post");
//        }
        return addMediaFileCommand.execute(files);
    }
}
