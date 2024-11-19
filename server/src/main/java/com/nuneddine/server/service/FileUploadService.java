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
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final AmazonS3 amazonS3;

    @Value("${s3.bucket-name}")
    private String bucketName;

    // DTO에서 받은 base64 인코딩 된 값을 매개변수로 입력
    @Transactional
    public String uploadImage(String base64Image) {
        byte[] decodedBytes;
        try {
            decodedBytes = Base64.getDecoder().decode(base64Image.split(",")[1]); // "data:image/png;base64," 제거
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_BASE64_DATA);
        }
        String fileName = "images/" + UUID.randomUUID() + ".png";

        // S3에 저장할 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(decodedBytes.length);
        metadata.setContentType("image/png");

        // 파일 업로드
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes)) {
            amazonS3.putObject(bucketName, fileName, inputStream, metadata);
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
