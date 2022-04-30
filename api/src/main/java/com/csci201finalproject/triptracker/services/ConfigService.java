package com.csci201finalproject.triptracker.services;

import org.springframework.stereotype.Service;

@Service
public class ConfigService {
    public final String DEFAULT_S3_BUCKET = "tripline-csci201-v2";

    /**
     * This will now only return the default S3 bucket but it might change in the
     * future which is why we put it in a getter
     * 
     * @return the S3 bucket name
     */
    public String getS3BucketName() {
        return this.DEFAULT_S3_BUCKET;
    }

}
