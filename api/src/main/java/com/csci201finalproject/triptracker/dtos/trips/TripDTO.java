package com.csci201finalproject.triptracker.dtos.trips;

import java.util.List;

public class TripDTO {
    private String title;
    private String description;
    private LocationDTO location;
    private String author;
    private String from;
    private String to;
    private List<EventDTO> events;
    private List<PhotoDTO> photos;
}
