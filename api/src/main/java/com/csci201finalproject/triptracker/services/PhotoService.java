package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.dtos.trips.PhotoDTO;
import com.csci201finalproject.triptracker.entities.EventEntity;
import com.csci201finalproject.triptracker.entities.PhotoEntity;
import com.csci201finalproject.triptracker.entities.TripEntity;
import com.csci201finalproject.triptracker.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoService {
    @Autowired
    PhotoRepository photoRepository;
    public PhotoEntity createPhoto(PhotoDTO photoDTO, TripEntity trip) {
        PhotoEntity photo = new PhotoEntity();
        photo.setCaption(photoDTO.getCaption());
        photo.setObjectKeyAws(photoDTO.getObject_key_aws());
        photo.setTrip(trip);

        photo = photoRepository.save(photo);
        return photo;
    }

    public PhotoEntity createPhoto(PhotoDTO photoDTO, EventEntity event) {
        PhotoEntity photo = new PhotoEntity();
        photo.setCaption(photoDTO.getCaption());
        photo.setObjectKeyAws(photoDTO.getObject_key_aws());
        photo.setEvent(event);

        photo = photoRepository.save(photo);
        return photo;
    }
    public List<PhotoEntity> createPhotos(List<PhotoDTO> photoDTOS, TripEntity trip) {
        List<PhotoEntity> photos = new ArrayList<>();
        for(PhotoDTO photoDTO : photoDTOS) {
            photos.add(createPhoto(photoDTO, trip));
        }
        return photos;
    }

    public List<PhotoEntity> createPhotos(List<PhotoDTO> photoDTOS, EventEntity event) {
        List<PhotoEntity> photos = new ArrayList<>();
        for(PhotoDTO photoDTO : photoDTOS) {
            photos.add(createPhoto(photoDTO, event));
        }
        return photos;
    }
}
