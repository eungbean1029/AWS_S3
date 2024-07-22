package com.eungbean.example.s3Test.s3MockTest.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//@Configuration
public class TestAwsS3MockConfig {

    private final int port = 9000;

    @Bean
    public S3Mock s3Mock() {
        return new S3Mock.Builder().withPort(port).withInMemoryBackend().build();
    }

    @Bean
    public AmazonS3 amazonS3() {
        String endpoint = "http://localhost:" + port;
        return AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, Regions.AP_SOUTHEAST_2.name()))
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
    }
}
