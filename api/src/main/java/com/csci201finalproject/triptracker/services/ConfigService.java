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

    /**
     * This will now only return the default S3 bucket but it might change in the
     * future which is why we put it in a getter
     * 
     * @return the S3 bucket name
     */
    public String getS3BucketName() {
        return this.s3DefaultBucket;
    }

    public AwsBasicCredentials getAwsBasicCredentials() {
        return AwsBasicCredentials.create(getAccessKeyId(), getSecretAccessKey());
    }
}
