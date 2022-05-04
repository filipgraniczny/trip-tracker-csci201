package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.entities.TripEntity;
import com.csci201finalproject.triptracker.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TripService {
    @Autowired
    TripRepository tripRepository;

    public List<TripEntity> getTrips(int limit, String searchTerm, String sortBy, boolean ascending) {
        Sort.Direction direction;
        if(ascending) {
            direction = Sort.Direction.ASC;
        }
        else {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(0, limit, Sort.by(direction, sortBy));
        return tripRepository.findAllByTitleContaining(searchTerm, pageable);
    }

    public boolean deleteTrip(int id) {
        // check if item with passed id exists and return whether delete was successful
        if(tripRepository.existsById(id)) {
            tripRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }

}
