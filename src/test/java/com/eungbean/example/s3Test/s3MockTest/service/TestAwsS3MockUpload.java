package com.eungbean.example.s3Test.s3MockTest.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.eungbean.example.s3Test.s3MockTest.config.TestAwsS3MockConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(TestAwsS3MockConfig.class)
public class TestAwsS3MockUpload {

    @Autowired
    private S3Mock s3Mock;

    @Autowired
    private AmazonS3 amazonS3;
    private final String bucketName = "test-bucket";
    private final String key = "testFile.txt";

    @BeforeAll
    public static void startS3Mock(@Autowired S3Mock s3Mock) {
        s3Mock.start();
    }

    @AfterAll
    public static void stopS3Mock(@Autowired S3Mock s3Mock) {
        s3Mock.stop();
    }

//    @Test
    @DisplayName("Multipart Upload File")
    public void MultipartUploadFile() throws IOException {
        amazonS3.createBucket(bucketName);
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult response = amazonS3.initiateMultipartUpload(request);
        String uploadId = response.getUploadId();

        // Multipart Upload 수행
        byte[] testData = "test01".getBytes();
        UploadPartRequest uploadPartRequest = new UploadPartRequest()
                .withBucketName(bucketName)
                .withKey(key)
                .withUploadId(uploadId)
                .withPartNumber(1)
                .withInputStream(new ByteArrayInputStream(testData))
                .withPartSize(testData.length);

        UploadPartResult uploadPartResult = amazonS3.uploadPart(uploadPartRequest);
        PartETag partETag = uploadPartResult.getPartETag();
        System.out.println("partETag: " + partETag.getETag());

        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest()
                .withBucketName(bucketName)
                .withKey(key)
                .withUploadId(uploadId)
                .withPartETags(Collections.singletonList(partETag));

        amazonS3.completeMultipartUpload(completeMultipartUploadRequest);
        System.out.println("completeMultipartUploadRequest: " + completeMultipartUploadRequest);
        assertNotNull(amazonS3.getObject(bucketName, key));
        assertEquals(amazonS3.getObject(bucketName, key).getObjectMetadata().getContentLength(), 6);

        // 파일 다운로드
        S3Object s3Object = amazonS3.getObject(bucketName, key);
        S3ObjectInputStream objectContent = s3Object.getObjectContent();
        File downloadFile = new File("downloadedTestFile.txt");
        saveToFile(objectContent, downloadFile);

        assertNotNull(downloadFile);
        assertEquals(downloadFile.length(), 6);

        // 파일 내용 확인
        String content = Files.readString(downloadFile.toPath());
        System.out.println("content: " + content);
        assertEquals("test01", content);

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
            e.printStackTrace();
        }
    }

}


