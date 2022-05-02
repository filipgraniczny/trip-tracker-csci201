package com.csci201finalproject.triptracker.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.csci201finalproject.triptracker.dtos.auth.RegisterDTO;
import com.csci201finalproject.triptracker.entities.PhotoEntity;
import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.repositories.PhotoRepository;
import com.csci201finalproject.triptracker.repositories.UserRepository;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service()
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private ConfigService configService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private PhotoRepository photoRepository;

    /**
     * Creates new user account and returns the user non-credentials
     * 
     * @param userData - `RegisterDTO` instance; has email, password, and name
     * @return user entity
     */
    public UserEntity createUser(RegisterDTO userData) {
        UserEntity user = new UserEntity();
        user.setEmail(userData.getEmail());
        user.setName(userData.getName());
        user.setPassword(userData.getPassword());
        user = userRepository.save(user);
        user.setPassword(null); // protects credential field
        return user;
    }

    /**
     * Verifies the user email or password and returns the User object with no
     * credentials e.g password
     * 
     * @param email
     * @param password
     * @return null if email or password is invalid else returns the obfuscated user
     *         entity
     */
    public UserEntity verifyUserByEmailAndPassword(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        if (!Objects.isNull(user) && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * Retrieve user general information via their ID
     * 
     * @param id
     * @return UserEntity if user under ID is found else null
     */
    public UserEntity findUserById(Integer id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty())
            return null;
        try {
            return user.get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Update the profile image of user via uploading the photo to S3, adding a
     * PhotoEntity and linking this entity to the userEntity
     * 
     * If user has no profile image initially this adds their profile image
     * 
     * By default, the name of the AWS S3 object will follow the format of:
     * <code>pfp_[USER ID]_[RANDOM UUID]</code>
     * 
     * @param userEntity - the user Entity
     * @param multipart  - a MultipartFile object of the original file
     * @return [the presigned URL as a URL object of the new image, the
     *         created PhotoEntity]
     * @throws IllegalArgumentException if user is null or does not exist in
     *                                  database or if File is not an acceptable
     *                                  image format
     * @throws IOException              if any unexpected errors occur
     */
    public List<Object> updateProfileImageUser(UserEntity userEntity, MultipartFile multipart)
            throws IllegalArgumentException, IOException {

        if (Objects.isNull(userEntity.getId())) {
            throw new IllegalArgumentException("Invalid UserEntity passed in; ID = null");
        }
        UserEntity foundUserEntity = findUserById(userEntity.getId());
        if (userEntity == null || foundUserEntity == null) {
            throw new IllegalArgumentException("Invalid UserEntity passed in; UserEntity must exist in database");
        }
        // standard AWS filename for profile photo on S3
        String objectAwsKey = String.format("pfp_%s_%s.%s", foundUserEntity.getId(), UUID.randomUUID().toString(),
                multipart.getContentType().split("/")[1]);

        // get mime type and check if it's a PNG or JPG and reject upload if not
        try {
            String mime = multipart.getContentType();
            if (!utilService.isValidProfileMimeType(mime)) {
                String errorMsg = String.format("Invalid file mime of %s; must be one of %s", mime,
                        utilService.validUserProfileMimeTypes);
                throw new IllegalArgumentException(errorMsg);
            }
        } catch (InvalidPathException invalidPathException) {
            invalidPathException.printStackTrace();
            throw new IllegalArgumentException(
                    "Error: filepath of photoFile could not be reconstructed; " + invalidPathException.getStackTrace());
        }

        // if user has profile picture then edit to db + delete S3
        PhotoEntity photoEntity = null;
        photoEntity = foundUserEntity.getProfilePhotoEntity();
        if (photoEntity != null) { // TODO: make this perform in parallel?
            // delete from s3 to free up space
            s3Service.deleteObject(configService.getS3BucketName(), photoEntity.getObjectKeyAws());
        } else {
            photoEntity = new PhotoEntity();
        }

        // perform uploading to S3
        s3Service.uploadObjectFromMultipart(configService.getS3BucketName(), objectAwsKey, multipart);

        // set new object key aws + save new name + presigned URL to record
        photoEntity.setObjectKeyAws(objectAwsKey);
        URL presignedUrl = s3Service.getObjectURLFromKey(configService.getS3BucketName(), objectAwsKey);
        photoEntity.setPresignedUrl(presignedUrl.toString());
        photoRepository.save(photoEntity);

        // save photo entity to user record
        foundUserEntity.setProfilePhotoEntity(photoEntity);
        userRepository.save(foundUserEntity);

        // return url
        URL objectUrl = s3Service.getObjectURLFromKey(configService.getS3BucketName(), objectAwsKey);
        return List.of(objectUrl, photoEntity);
    }
}
