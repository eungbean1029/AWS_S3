package com.eungbean.example.s3Test.s3.controller;

import com.eungbean.example.s3Test.s3.services.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestBody byte[] file) {
        return awsS3Service.upload(file);
    }
}
