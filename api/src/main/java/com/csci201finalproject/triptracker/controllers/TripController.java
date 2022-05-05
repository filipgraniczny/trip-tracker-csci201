package com.csci201finalproject.triptracker.controllers;

import com.csci201finalproject.triptracker.entities.PhotoEntity;
import com.csci201finalproject.triptracker.dtos.trips.TripDTO;
import com.csci201finalproject.triptracker.entities.TripEntity;
import com.csci201finalproject.triptracker.interfaces.ErrorResponseClass;
import com.csci201finalproject.triptracker.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/trips")
public class TripController {
    @Autowired
    TripService tripService;

    @GetMapping("/explore")
    public @ResponseBody Object getTrips(@RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "toTime", required = false) String sortBy,
            @RequestParam(defaultValue = "true", required = false) boolean ascending) {
        List<TripEntity> trips = tripService.getTrips(limit, searchTerm, sortBy, ascending);
        if (Objects.isNull(trips) || trips.isEmpty())
            return new ErrorResponseClass(false, "NOT_FOUND", "No trips found with this search term");
        return trips;
    }

    @DeleteMapping("/{id}")
    public @ResponseBody Object deleteTrip(@PathVariable Integer id) {
        boolean success = tripService.deleteTrip(id);
        return "{\"success\":" + success + "}";
    }

    @PutMapping("/{id}/photos")
    public ResponseEntity<?> uploadTripPhotos(@PathVariable("id") Integer id,
            @RequestParam("files") MultipartFile[] files) {
        try {
            List<PhotoEntity> photoEntities = tripService.uploadTripPhotos(id, files);
            return ResponseEntity.ok().body(photoEntities);
        } catch (IllegalArgumentException e) {
            ErrorResponseClass errorBody = new ErrorResponseClass(false, "BAD_REQUEST", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
        } catch (AwsServiceException | SdkClientException | IOException e) {
            ErrorResponseClass errorBody = new ErrorResponseClass(false, "AWS_ERROR", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
        }
    }

    @PostMapping("/")
    public @ResponseBody Object addTrip(@RequestBody TripDTO tripDTO) {
        try {
            TripEntity trip = tripService.createTrip(tripDTO);

        } catch (Exception e) {
            ErrorResponseClass error = new ErrorResponseClass(false, "INVALID_TRIP", "Invalid trip object");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return "{\"success\": true }";
    }
}
