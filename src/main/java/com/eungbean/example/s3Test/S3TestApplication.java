package com.eungbean.example.s3Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(value = "com.eungbean.example.s3Test")
public class S3TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3TestApplication.class, args);
	}

}
