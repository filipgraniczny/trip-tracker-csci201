package com.csci201finalproject.triptracker.controllers;

import com.csci201finalproject.triptracker.dtos.auth.LoginDTO;
import com.csci201finalproject.triptracker.dtos.auth.RegisterDTO;
import com.csci201finalproject.triptracker.entities.TripEntity;
import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.interfaces.ErrorResponseClass;
import com.csci201finalproject.triptracker.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
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

    @PostMapping("/")
    public @ResponseBody Object addTrip(@RequestBody TripEntity trip){
        TripEntity newTrip = tripService.save(trip);
        return "{\"success\": true }";
    }
}
