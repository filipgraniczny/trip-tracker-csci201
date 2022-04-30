package com.csci201finalproject.triptracker.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UtilService {
    public List<String> validUserProfileMimeTypes = List.of("image/png", "image/jpg", "image/jpeg");

    public boolean isValidProfileMimeType(String mime) {

        return Arrays.stream(validUserProfileMimeTypes.toArray()).anyMatch(x -> mime.equals(x));
    }
}
