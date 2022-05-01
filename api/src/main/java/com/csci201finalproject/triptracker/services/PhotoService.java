package com.csci201finalproject.triptracker.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class PhotoService {
    private static final S3Client s3 = S3Client.builder().region(Region.US_WEST_1).build();

    /**
     * Get the default S3 client to use
     * 
     * @return a S3Client object
     */
    public S3Client getS3Client() {
        return s3;
    }

    /**
     * Gets the object URL object; can be used to get public presigned URL for
     * displaying image purposes
     * 
     * @param key    Name of the object in the S3 bucket
     * @param bucket Name of the bucket
     * @return the URL object
     * @throws SdkException if the URL is malformed
     */
    public URL getObjectURLFromKey(String bucket, String key) throws SdkException {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder().bucket(bucket).key(key).build();
        URL objectUrl = s3.utilities().getUrl(getUrlRequest);
        return objectUrl;
    }

    /**
     * Uploads the File to S3 bucket
     * 
     * @param bucket the bucket name
     * @param key    the file key in the bucket
     * @param file   File object
     */
    public void uploadObject(String bucket, String key, File file)
            throws AwsServiceException, SdkClientException, S3Exception {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(key).build();
        s3.putObject(putObjectRequest, RequestBody.fromFile(file));
    }

    /**
     * Uploads the MultipartFile to S3 bucket; useful for dealing with form-data
     * requests
     * 
     * @param bucket
     * @param key
     * @param multipartFile
     * @throws S3Exception
     * @throws AwsServiceException
     * @throws SdkClientException
     * @throws IOException
     */
    public void uploadObjectFromMultipart(String bucket, String key, MultipartFile multipartFile)
            throws S3Exception, AwsServiceException, SdkClientException, IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).key(key)
                .contentType(multipartFile.getContentType()).build();
        s3.putObject(putObjectRequest,
                RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
    }
}
