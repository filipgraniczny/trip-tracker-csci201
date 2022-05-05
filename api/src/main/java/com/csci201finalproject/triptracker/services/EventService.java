package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.dtos.trips.EventDTO;
import com.csci201finalproject.triptracker.entities.EventEntity;
import com.csci201finalproject.triptracker.entities.TripEntity;
import com.csci201finalproject.triptracker.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import static com.csci201finalproject.triptracker.util.Timestamp.timestampToDate;

@Service
public class EventService {
    @Autowired
    LocationService locationService;
    @Autowired
    PhotoService photoService;
    @Autowired
    EventRepository eventRepository;

    public List<EventEntity> createEvents(List<EventDTO> eventDTOS, TripEntity trip) {
        List<EventEntity> events = new ArrayList<>();
        for(EventDTO eventDTO : eventDTOS) {
            events.add(createEvent(eventDTO, trip));
        }
        return events;
    }

    public EventEntity createEvent(EventDTO eventDTO, TripEntity trip) {
        EventEntity event = new EventEntity();
        event.setName(eventDTO.getName());
        event.setCategory(eventDTO.getCategory());
        event.setDescription(eventDTO.getDescription());

        Date from_date = timestampToDate(eventDTO.getFrom());
        event.setFromTime(new Timestamp(from_date.getTime()));

        Date to_date = timestampToDate(eventDTO.getTo());
        event.setToTime(new Timestamp(to_date.getTime()));

        event.setTrip(trip);
        event.setLocation(locationService.createLocation(eventDTO.getLocation()));
        event.setPhotos(photoService.createPhotos(eventDTO.getPhotos(), event));


        event = eventRepository.save(event);

        return event;
    }

}
