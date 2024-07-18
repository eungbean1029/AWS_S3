package com.eungbean.example.s3Test.s3.services;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    // 1. Stream upload - 1회 API 호출에 1개 파일만
    // 2. Multipart upload
    // 3. AWS Multi-part upload
    public ResponseEntity<String> upload(byte[] file) {
        String key = "testupload/" + UUID.randomUUID() + ".jpg";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpeg");

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, new ByteArrayInputStream(file), objectMetadata);
        amazonS3Client.putObject(putObjectRequest);
        return ResponseEntity.ok(key);
    }

}
