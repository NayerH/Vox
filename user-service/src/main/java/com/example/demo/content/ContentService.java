package com.example.demo.content;

import com.example.demo.token.ContentRepository;
import com.example.demo.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository repository;


//    public InappropriateContent reportContent(Integer id) {
//        var content= repository.findById(id)
//                .map(inappropriateContent -> {
//
//                    inappropriateContent.setReportsNum(inappropriateContent.getReportsNum()+1);
//                   return repository.save(inappropriateContent);
//                });
//        var newContent=InappropriateContent.builder().id(id).reportsNum(1).build();
//
//        return repository.save(newContent);
//    }

    public InappropriateContent reportContent(Integer id) {
        Optional<InappropriateContent> existingContent = repository.findById(id);

        if (existingContent.isPresent()) {
            InappropriateContent content = existingContent.get();
            content.setReportsNum(content.getReportsNum() + 1);
            return repository.save(content);
        } else {
            InappropriateContent newContent = InappropriateContent.builder()
                    .id(id)
                    .reportsNum(1)
                    .build();
            return repository.save(newContent);
        }
    }

}

