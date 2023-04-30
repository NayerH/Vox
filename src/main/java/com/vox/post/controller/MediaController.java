package com.vox.post.controller;

import com.vox.post.model.Media;
import com.vox.post.model.MediaFile;
import com.vox.post.service.interfaces.ReturnManyMediaCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "api/v1/media")
public class MediaController {
    @Autowired
    private ReturnManyMediaCommand addMediaCommand;

    @PostMapping("/add")
    public Media addMedia(@RequestBody List<MediaFile> mediaFiles) {
        System.out.println(mediaFiles);
        return addMediaCommand.execute(new Media(mediaFiles, "userId"));
    }

}
