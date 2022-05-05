package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.entities.PhotoEntity;
import com.csci201finalproject.triptracker.entities.TripEntity;
import com.csci201finalproject.triptracker.repositories.PhotoRepository;
import com.csci201finalproject.triptracker.dtos.trips.TripDTO;
import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.repositories.TripRepository;
import com.csci201finalproject.triptracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TripService {
    @Autowired
    TripRepository tripRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private ConfigService configService;
    @Autowired
    private UtilService utilService;
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
        if (ascending) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(0, limit, Sort.by(direction, sortBy));
        return tripRepository.findAllByTitleContaining(searchTerm, pageable);
    }

    public boolean deleteTrip(int id) {
        // check if item with passed id exists and return whether delete was successful
        if (tripRepository.existsById(id)) {
            tripRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find trip by ID
     * 
     * @param id
     * @return null if no trip under this ID and the TripEntity else
     */
    public TripEntity findTripById(Integer id) {
        Optional<TripEntity> tripEntity = tripRepository.findById(id);
        if (tripEntity.isEmpty())
            return null;
        try {
            return tripEntity.get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Upload images to this event
     * AWS keys should be `trip_{ID}_{RANDOM_UUID}`
     * 
     * @param id    - Event ID
     * @param files - MultipartFiles
     * @return list of PhotoEntities
     * @throws IOException
     * @throws SdkClientException
     * @throws AwsServiceException
     * @throws S3Exception
     */
    public List<PhotoEntity> uploadTripPhotos(Integer id, Iterable<MultipartFile> files)
            throws IllegalArgumentException, S3Exception, AwsServiceException, SdkClientException, IOException {
        // validate if there are files at all
        if (Objects.isNull(files)) {
            throw new IllegalArgumentException("Must provide 1 or more files");
        }

        // verify images are of valid format
        utilService.verifyAllImagesValid(files);

        TripEntity tripEntity = findTripById(id);
        // verify if this TripEntity is null
        if (Objects.isNull(tripEntity) || Objects.isNull(tripEntity.getId())) {
            throw new IllegalArgumentException("Invalid tripEntity passed in");
        }

        // a few variables
        String s3Bucket = configService.getS3BucketName();
        List<String> keyFiles = new ArrayList<>();
        List<PhotoEntity> photoEntities = new ArrayList<>();

        // through all files
        for (MultipartFile file : files) {
            String key = String.format("trip_%s_%s.%s", tripEntity.getId(), UUID.randomUUID(),
                    file.getContentType().split("/")[1]);
            keyFiles.add(key);

            // upload to S3
            s3Service.uploadObjectFromMultipart(s3Bucket, key, file);

            // add to the batch of entities to save
            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setObjectKeyAws(key);
            photoEntity.setTrip(tripEntity);
            // get presigned url to save to DB
            String entityPresignedURL = s3Service.getObjectURLFromKey(s3Bucket, photoEntity.getObjectKeyAws())
                    .toString();
            photoEntity.setPresignedUrl(entityPresignedURL);
            photoEntities.add(photoEntity);
        }
        Iterable<PhotoEntity> photoEntitiesIterable = photoRepository.saveAll(photoEntities);

        // save each one to the original list
        int i = 0;
        for (PhotoEntity savedEntity : photoEntitiesIterable) {
            // stop if it happens to go over to avoid out of bound
            if (i > photoEntities.size() - 1) {
                break;
            }
            photoEntities.set(i, savedEntity);
            i++;
        }

        return photoEntities;
    }

    public TripEntity createTrip(TripDTO tripDTO) {
        TripEntity trip = new TripEntity();
        trip.setTitle(tripDTO.getTitle());
        trip.setDescription(tripDTO.getDescription());
        trip.setLocation(locationService.createLocation(tripDTO.getLocation()));
        Optional<UserEntity> author = userRepository.findById(tripDTO.getAuthor());
        if (author.isPresent()) {
            trip.setAuthor(author.get());
        } else {
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
        photoService.createPhotos(tripDTO.getPhotos(), trip);

        return trip;
    }

    public TripEntity save(TripEntity t) {
        return tripRepository.save(t);
    }
}
