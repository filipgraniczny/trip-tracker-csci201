package com.csci201finalproject.triptracker.dtos.trips;

import java.util.List;

public class EventDTO {
    private String name;
    private String category;
    private String description;
    private String from;
    private String to;
    private LocationDTO location;
    private List<PhotoDTO> photos;
}
