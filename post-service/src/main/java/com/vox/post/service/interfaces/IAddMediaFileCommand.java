package com.vox.post.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

public interface IAddMediaFileCommand extends Command {

    ArrayList<String> execute(ArrayList<MultipartFile> files) throws IOException;
}
