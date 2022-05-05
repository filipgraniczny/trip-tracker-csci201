package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.dtos.trips.PhotoDTO;
import com.csci201finalproject.triptracker.entities.PhotoEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoService {
    public PhotoEntity createPhoto(PhotoDTO photoDTO) {
        PhotoEntity photo = new PhotoEntity();
        photo.setCaption(photoDTO.getCaption());
        photo.setObjectKeyAws(photoDTO.getObject_key_aws());
        return photo;
    }
    public List<PhotoEntity> createPhotos(List<PhotoDTO> photoDTOS) {
        List<PhotoEntity> photos = new ArrayList<>();
        for(PhotoDTO photoDTO : photoDTOS) {
            photos.add(createPhoto(photoDTO));
        }
        return photos;
    }
}
