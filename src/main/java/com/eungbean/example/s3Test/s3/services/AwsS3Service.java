package com.eungbean.example.s3Test.s3.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.eungbean.example.s3Test.s3.dto.DownloadFileRequest;
import com.eungbean.example.s3Test.s3.dto.DownloadFileResponse;
import com.eungbean.example.s3Test.s3.dto.UploadFileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3Service {

    @Value("${aws.s3.bucket}")
    private String bucket;
    @Value("${aws.s3.path}")
    private String path;

    private final AmazonS3 amazonS3;

    public DownloadFileResponse upload(UploadFileRequest uploadFileRequest) throws IOException {
        log.info("Upload file: {}", uploadFileRequest.file());
        log.info("Upload file Initialized");

        // 파일 Key 생성
        String key = generateFileKey(uploadFileRequest.file().getOriginalFilename());
        // 파일 MetaData 생성
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(uploadFileRequest.file().getContentType());
        objectMetadata.setContentLength(uploadFileRequest.file().getSize());

        try {
            // 파일 업로드 요청 객체 생성
            PutObjectRequest putObjectRequest = makePutObjectRequest(uploadFileRequest.file(), key, objectMetadata);
            // 파일 업로드
            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            log.error("upload file error: {}", e.getMessage());
            throw e;
        }
        return DownloadFileResponse.builder()
                .key(key)
                .build();
    }

    public Object download(DownloadFileRequest downloadFileRequest) {
        log.info("Download file: {}", downloadFileRequest.key());
        log.info("Download file Initialized");
        // 해당 파일 가져오기
        S3Object s3Object = amazonS3.getObject(bucket, downloadFileRequest.key());
        // 파일 스트림 가져오기
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

        File downloadFile = new File(downloadFileRequest.key());
        saveToFile(s3ObjectInputStream, downloadFile);
        log.info("download file: {}", downloadFile.getName());

        return ResponseEntity.ok();
    }

    private PutObjectRequest makePutObjectRequest(MultipartFile file, String key, ObjectMetadata objectMetadata) throws IOException {
        return new PutObjectRequest(
                bucket,
                key,
                file.getInputStream(),
                objectMetadata
        );
    }

    // 파일 Key는 "폴더 경로 + 파일명"
    private String generateFileKey(String fileName) {
        return path + "/" + fileName;
    }

    private void saveToFile(S3ObjectInputStream objectContent, File downloadFile) {
        // 파일 저장
        try (FileOutputStream fileOutputStream = new FileOutputStream(downloadFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = objectContent.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            //TODO : 예외 처리
            e.printStackTrace();
        }
    }

}
