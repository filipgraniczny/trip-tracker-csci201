package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.dtos.trips.EventDTO;
import com.csci201finalproject.triptracker.entities.EventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    @Autowired
    LocationService locationService;
    @Autowired
    PhotoService photoService;

    public List<EventEntity> createEvents(List<EventDTO> eventDTOS) {
        List<EventEntity> events = new ArrayList<>();
        for(EventDTO eventDTO : eventDTOS) {
            events.add(createEvent(eventDTO));
        }
        return events;
    }

    public EventEntity createEvent(EventDTO eventDTO) {
        EventEntity event = new EventEntity();
        event.setName(eventDTO.getName());
        event.setCategory(eventDTO.getCategory());
        event.setDescription(eventDTO.getDescription());
        event.setFromTime(Timestamp.valueOf(eventDTO.getFrom()));
        event.setToTime(Timestamp.valueOf(eventDTO.getTo()));
        event.setLocation(locationService.createLocation(eventDTO.getLocation()));
        event.setPhotos(photoService.createPhotos(eventDTO.getPhotos()));

        return event;
    }
}
