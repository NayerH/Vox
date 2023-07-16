package com.example.demo.content;

import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.auth.AuthenticationService;
import com.example.demo.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ContentController {
    private final ContentService service;

    @PostMapping("/report_content")
    public ResponseEntity<InappropriateContent> reportContent(@RequestBody Map<String, Integer> requestBody) {
        var id= requestBody.get("id");
        System.out.println("Rana  :"+ id);
        return ResponseEntity.ok(service.reportContent(id));
    }

}
