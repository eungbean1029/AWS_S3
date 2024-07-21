package com.eungbean.example.s3Test.s3MockTest.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.eungbean.example.s3Test.s3MockTest.config.TestAwsS3MockConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(TestAwsS3MockConfig.class)
public class TestAwsS3MockDownload {
    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private S3Mock s3Mock;

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
    public void testMultipartDownloadFile() {
        System.out.println("bucketName: " + bucketName);
        amazonS3.createBucket(bucketName);
        S3Object s3Object = amazonS3.getObject(bucketName, key);
        S3ObjectInputStream objectContent = s3Object.getObjectContent();
        File downloadFile = new File("downloadFile01.txt");
        saveToFile(objectContent, downloadFile);

        assertNotNull(downloadFile);
        assertEquals(downloadFile.length(), 6);
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
