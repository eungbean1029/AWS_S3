package com.eungbean.example.s3Test.s3.controller;

import com.eungbean.example.s3Test.s3.dto.DownloadFileRequest;
import com.eungbean.example.s3Test.s3.dto.DownloadFileResponse;
import com.eungbean.example.s3Test.s3.dto.UploadFileRequest;
import com.eungbean.example.s3Test.s3.services.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * AWS S3 Controller Test
 */
@RequestMapping("aws/s3")
@RequiredArgsConstructor
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    /**
     * AWS S3 파일 업로드
     */
    @PostMapping("/upload")
    public DownloadFileResponse upload(UploadFileRequest uploadFileRequest) throws IOException {
        return awsS3Service.upload(uploadFileRequest);
    }

    /**
     * AWS S3 파일 다운로드
     */
    @PostMapping("/download")
    public ResponseEntity<Object> download(DownloadFileRequest downloadFileRequest) {
        return ResponseEntity.ok(awsS3Service.download(downloadFileRequest));
    }
}
