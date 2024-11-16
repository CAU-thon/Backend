package com.nuneddine.server.controller;

import com.nuneddine.server.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class S3TestController {

    private final FileUploadService fileUploadService;

    @PostMapping("")
    public ResponseEntity<?> uploadImage(@RequestBody String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return ResponseEntity.badRequest().body("Base64 image data가 필요합니다.");
        }
        String fileUrl = fileUploadService.uploadImage(base64Image);
        return ResponseEntity.ok(fileUrl);
    }
}
