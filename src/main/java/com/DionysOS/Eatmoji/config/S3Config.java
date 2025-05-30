package com.DionysOS.Eatmoji.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Bean
    public AmazonS3 amazonS3() {
        Dotenv dotenv = Dotenv.load();
        BasicAWSCredentials credentials = new BasicAWSCredentials(
                dotenv.get("AWS_ACCESS_KEY_ID"),
                dotenv.get("AWS_SECRET_ACCESS_KEY")
        );

        return AmazonS3ClientBuilder.standard()
                .withRegion(dotenv.get("REGION"))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}