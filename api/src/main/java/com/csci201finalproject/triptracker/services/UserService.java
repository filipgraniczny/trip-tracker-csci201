package com.csci201finalproject.triptracker.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.csci201finalproject.triptracker.dtos.auth.RegisterDTO;
import com.csci201finalproject.triptracker.entities.PhotoEntity;
import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.repositories.PhotoRepository;
import com.csci201finalproject.triptracker.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhotoService photoService;
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
            user.setPassword(null); // obscure this credential field
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
            user.get().setPassword(null);
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
     * @param photoFile  - a File object of the photo
     * @return the presigned URL as a URL object of the new image
     * @throws IllegalArgumentException if user is null or does not exist in
     *                                  database or if File is not an acceptable
     *                                  image format
     * @throws IOException              if any unexpected errors occur
     */
    public URL updateProfileImageUser(UserEntity userEntity, File photoFile)
            throws IllegalArgumentException, IOException {
        if (findUserById(userEntity.getId()) == null) {
            throw new IllegalArgumentException("Invalid UserEntity passed in; UserEntity must exist in database");
        }

        // get mime type and check if it's a PNG or JPG and reject upload if not
        try {
            Path filepath = photoFile.toPath();
            String mime = Files.probeContentType(filepath);
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

        // perform uploading to S3
        String objectAwsKey = String.format("pfp_%s_%s", userEntity.getId(), UUID.randomUUID().toString());
        photoService.uploadObject(configService.getS3BucketName(), objectAwsKey, photoFile);

        // create photo db record based on AWS key
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setObjectKeyAws(objectAwsKey);
        photoRepository.save(photoEntity);

        // save photo entity to user record
        userEntity.setProfilePhotoEntity(photoEntity);
        userRepository.save(userEntity);

        // return url
        URL objectUrl = photoService.getObjectURLFromKey(configService.getS3BucketName(), objectAwsKey);
        return objectUrl;
    }
}
