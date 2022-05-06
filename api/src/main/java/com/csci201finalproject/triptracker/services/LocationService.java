package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.dtos.trips.LocationDTO;
import com.csci201finalproject.triptracker.entities.LocationEntity;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    public LocationEntity createLocation(LocationDTO locationDTO) {
        LocationEntity location = new LocationEntity();
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setName(locationDTO.getName());
        return location;
    }
}
