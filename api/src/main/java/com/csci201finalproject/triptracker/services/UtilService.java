package com.csci201finalproject.triptracker.services;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UtilService {
    public List<String> validUserProfileMimeTypes = List.of("image/png", "image/jpg", "image/jpeg");

    public boolean isValidProfileMimeType(String mime) {
        return Arrays.stream(validUserProfileMimeTypes.toArray()).anyMatch(x -> mime.equals(x));
    }

    public void verifyAllImagesValid(Iterable<MultipartFile> files) throws IllegalArgumentException {
        for (MultipartFile file : files) {
            if (!isValidProfileMimeType(file.getContentType())) {
                String errorMessage = String.format("Invalid format %s for file %s", file.getContentType(),
                        file.getOriginalFilename());
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }
}
