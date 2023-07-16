package com.vox.post.controller;

import com.vox.post.service.MediaService;
import com.vox.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "api/v1/media")
@ConditionalOnProperty(prefix = "disable", name = "media.controller", havingValue = "false", matchIfMissing = true)
public class MediaController {

    private MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/add")
    public ArrayList<String> addMedia(HttpSession session, @RequestParam("files") ArrayList<MultipartFile> files) throws IOException {
        return mediaService.addMedia(session.getId(), files);
    }
}
