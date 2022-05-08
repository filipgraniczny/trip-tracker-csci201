package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.dtos.trips.EventDTO;
import com.csci201finalproject.triptracker.entities.EventEntity;
import com.csci201finalproject.triptracker.entities.PhotoEntity;
import com.csci201finalproject.triptracker.entities.TripEntity;
import com.csci201finalproject.triptracker.repositories.EventRepository;
import com.csci201finalproject.triptracker.repositories.PhotoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.csci201finalproject.triptracker.util.Timestamp.timestampToDate;

@Service
public class EventService {
    @Autowired
    LocationService locationService;
    @Autowired
    PhotoService photoService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private ConfigService configService;

    public List<EventEntity> createEvents(List<EventDTO> eventDTOS, TripEntity trip) {
        List<EventEntity> events = new ArrayList<>();
        for (EventDTO eventDTO : eventDTOS) {
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

        event = eventRepository.save(event);
        event.setLocation(locationService.createLocation(eventDTO.getLocation()));
        if (eventDTO.getPhotos() != null) {
            event.setPhotos(photoService.createPhotos(eventDTO.getPhotos(), event));
        }
        event = eventRepository.save(event);
        return event;
    }

    /**
     * Upload photos for an event
     * File name on S3 follows `event_{EVENT_ID}_{RANDOM_UUID}.{FILE_FORMAT}`
     * e.g `trip_1_{RANDOM_UUID}.png`
     * 
     * @param id
     * @param files
     * @return list of photo entities
     * @throws IOException
     * @throws SdkClientException
     * @throws AwsServiceException
     * @throws S3Exception
     * @throws IllegalArgumentException when there's no event with this ID
     * @throws TimeoutException         when the amount of time passed
     */
    public List<PhotoEntity> uploadEventPhotos(Integer id, MultipartFile[] files)
            throws S3Exception, AwsServiceException, SdkClientException, IOException, IllegalArgumentException,
            TimeoutException {
        // validate if there are files at all
        if (Objects.isNull(files) || files.length == 0) {
            throw new IllegalArgumentException("Must provide 1 or more files");
        }

        // initialize thread pool with as many threads as files
        ExecutorService executorService = Executors.newFixedThreadPool(files.length);

        // validate valid event entity (must have id field and in record)
        EventEntity foundEventEntity = findEventById(id);
        if (Objects.isNull(foundEventEntity)) {
            throw new IllegalArgumentException("Invalid eventEntity passed in");
        }

        // keep the successfully completed tasks
        ConcurrentLinkedQueue<PhotoEntity> photoEntities = new ConcurrentLinkedQueue<>();

        // through all files
        for (MultipartFile file : files) {
            Thread uploadImageEventRunner = new Thread(() -> {
                try {
                    PhotoEntity photoEntity = addEventPhoto(foundEventEntity, file);
                    photoEntities.add(photoEntity);
                } catch (AwsServiceException | SdkClientException | IOException e) {
                    // TODO: store this error somewhere to indicate this upload has failed
                    e.printStackTrace();
                }
            });

            executorService.execute(uploadImageEventRunner);
        }

        // initiate timeout timer
        executorService.shutdown();
        try {
            Integer TIMEOUT_SECONDS_UPLOADING_PHOTOS = 5;
            if (!executorService.awaitTermination(TIMEOUT_SECONDS_UPLOADING_PHOTOS, TimeUnit.SECONDS)) {
                // timed out
                executorService.shutdownNow();
                throw new TimeoutException("Image uploading on our end took too long. Please try again");
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

        // batch save successfully completed uploads
        Iterable<PhotoEntity> photoEntitiesIterable = photoRepository.saveAll(photoEntities);

        // save each one to the original list for return value
        List<PhotoEntity> returningPhotoEntities = new ArrayList<>();
        for (PhotoEntity savedEntity : photoEntitiesIterable) {
            returningPhotoEntities.add(savedEntity);
        }

        return returningPhotoEntities;
    }

    private PhotoEntity addEventPhoto(EventEntity eventEntity, MultipartFile file)
            throws S3Exception, AwsServiceException, SdkClientException, IOException {
        String s3Bucket = configService.getS3DefaultBucket();
        String key = String.format("event_%s_%s.%s", eventEntity.getId(), UUID.randomUUID(),
                file.getContentType().split("/")[1]);

        // upload to S3
        s3Service.uploadObjectFromMultipart(s3Bucket, key, file);

        // add to the batch of entities to save
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setObjectKeyAws(key);
        photoEntity.setEvent(eventEntity);
        // get presigned url to save to DB
        String entityPresignedURL = s3Service.getObjectURLFromKey(s3Bucket, photoEntity.getObjectKeyAws())
                .toString();
        photoEntity.setPresignedUrl(entityPresignedURL);

        return photoEntity;
    }

    public EventEntity findEventById(Integer id) {
        Optional<EventEntity> eventEntity = eventRepository.findById(id);
        if (eventEntity.isEmpty())
            return null;
        try {
            return eventEntity.get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
