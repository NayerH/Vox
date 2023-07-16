package com.vox.post.service.commands;

import com.vox.post.service.interfaces.IAddMediaFileCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

@Component
public class AddMediaFileCommand implements IAddMediaFileCommand {
    public ArrayList<String> execute(ArrayList<MultipartFile> files) throws IOException {
        ArrayList<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            String path = "C:\\Users\\DELL\\Desktop\\Scalable\\Media\\"+file.getOriginalFilename();
            file.transferTo(Paths.get(path));
            paths.add(path);
        }
        return paths;
    }
}
