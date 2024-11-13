package com.nuneddine.server.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.nuneddine.server.exception.CustomException;
import com.nuneddine.server.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final AmazonS3 amazonS3;

    @Value("${s3.bucket-name}")
    private String bucketName;

    public String uploadImage(MultipartFile file) {
        String fileName = "images/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // S3에 저장할 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // 파일 업로드
        try {
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        } catch (AmazonServiceException e) {
            throw new CustomException(ErrorCode.AWS_S3_ACCESS_DENIED);
        } catch (AmazonClientException e) {
            throw new CustomException(ErrorCode.AWS_S3_NOT_CONNECTED);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_UPLOAD_FILE);
        }

        // 업로드된 파일의 URL 반환
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
