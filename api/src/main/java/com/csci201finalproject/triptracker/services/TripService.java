package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.dtos.trips.TripDTO;
import com.csci201finalproject.triptracker.entities.TripEntity;
import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.repositories.TripRepository;
import com.csci201finalproject.triptracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    @Autowired
    TripRepository tripRepository;
    @Autowired
    LocationService locationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventService eventService;
    @Autowired
    PhotoService photoService;

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

    public TripEntity createTrip(TripDTO tripDTO) {
        TripEntity trip = new TripEntity();
        trip.setTitle(tripDTO.getTitle());
        trip.setDescription(tripDTO.getDescription());
        trip.setLocation(locationService.createLocation(tripDTO.getLocation()));
        Optional<UserEntity> author = userRepository.findById(tripDTO.getAuthor());
        if(author.isPresent()) {
            trip.setAuthor(author.get());
        }
        else {
            throw new IllegalArgumentException("User adding trip does not exist");
        }
        DateFormat formatter = new SimpleDateFormat("mm-dd-YYYY");
        try {
            Date from_date = formatter.parse(tripDTO.getFrom());
            trip.setFromTime(new Timestamp(from_date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Date to_date = formatter.parse(tripDTO.getTo());
            trip.setToTime(new Timestamp(to_date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tripRepository.save(trip);

        trip = tripRepository.save(trip);

        eventService.createEvents(tripDTO.getEvents(), trip);
        photoService.createPhotos(tripDTO.getPhotos());
//        trip.setEvents(eventService.createEvents(tripDTO.getEvents()));
//        trip.setPhotos(photoService.createPhotos(tripDTO.getPhotos()));

        return trip;
    }

    public TripEntity save(TripEntity t) {
        return tripRepository.save(t);
    }

}
