package com.csci201finalproject.triptracker.controllers;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.csci201finalproject.triptracker.entities.PhotoEntity;
import com.csci201finalproject.triptracker.interfaces.ErrorResponseClass;
import com.csci201finalproject.triptracker.services.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/{id}/photos")
    public ResponseEntity<?> uploadEventPhotos(@RequestParam(name = "files") MultipartFile[] files,
            @PathVariable("id") Integer id) {
        try {
            List<PhotoEntity> photoEntities = eventService.uploadEventPhotos(id, files);
            return ResponseEntity.ok().body(photoEntities);
        } catch (IllegalArgumentException e) {
            ErrorResponseClass errorBody = new ErrorResponseClass(false, "BAD_REQUEST", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
        } catch (AwsServiceException | SdkClientException | IOException e) {
            ErrorResponseClass errorBody = new ErrorResponseClass(false, "AWS_ERROR", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        } catch (TimeoutException e) {
            ErrorResponseClass errorBody = new ErrorResponseClass(false, "TIMED_OUT", e.getMessage());
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(errorBody);
        }

    }
}
