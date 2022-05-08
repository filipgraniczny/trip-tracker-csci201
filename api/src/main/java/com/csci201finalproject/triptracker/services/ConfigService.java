package com.csci201finalproject.triptracker.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;

@ConfigurationProperties(prefix = "aws")
@Configuration("configService")
@Service
@Component
public class ConfigService {
    private String s3DefaultBucket;
    private String accessKeyId;
    private String secretAccessKey;

    public String getS3DefaultBucket() {
        return this.s3DefaultBucket;
    }

    public void setS3DefaultBucket(String s3DefaultBucket) {
        this.s3DefaultBucket = s3DefaultBucket;
    }

    public String getAccessKeyId() {
        return this.accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return this.secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public AwsBasicCredentials getAwsBasicCredentials() {
        return AwsBasicCredentials.create(getAccessKeyId(), getSecretAccessKey());
    }
}
