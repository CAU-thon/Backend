package com.nuneddine.server.controller;

import com.nuneddine.server.exception.CustomException;
import com.nuneddine.server.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class S3TestController {

    private final FileUploadService fileUploadService;

    @PostMapping("")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileUploadService.uploadImage(file);
        return ResponseEntity.ok(fileUrl);
    }
}
