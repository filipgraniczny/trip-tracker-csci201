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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static com.csci201finalproject.triptracker.util.Timestamp.timestampToDate;

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
    public List<PhotoEntity> uploadTripPhotos(Integer id, List<MultipartFile> files)
            throws IllegalArgumentException, S3Exception, AwsServiceException, SdkClientException, IOException {
        // create a thread pool with size = number of files since we need that much
        // number of tasks to execute concurrently
        ExecutorService executorService = Executors.newFixedThreadPool(files.size());

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

        // concurrent queue to allow
        ConcurrentLinkedQueue<PhotoEntity> photoEntitiesConcurrQueue = new ConcurrentLinkedQueue<>();

        // through all files, PUT to S3 and save to DB record
        for (MultipartFile file : files) {
            Thread imageUploadAddRecordRunner = new Thread(() -> {
                try {
                    PhotoEntity photoEntity = uploadPhotoAddRecordTrip(file, tripEntity);
                    photoEntitiesConcurrQueue.add(photoEntity);
                } catch (AwsServiceException | SdkClientException | IOException e) {
                    e.printStackTrace();
                }
            });

            executorService.execute(imageUploadAddRecordRunner);
        }

        Iterable<PhotoEntity> photoEntitiesIterable = photoRepository.saveAll(photoEntitiesConcurrQueue);

        List<PhotoEntity> photoEntitiesReturn = new ArrayList<>();
        // save each one back to a list for returning value
        for (PhotoEntity savedEntity : photoEntitiesIterable) {
            photoEntitiesConcurrQueue.add(savedEntity);
        }

        return photoEntitiesReturn;
    }

    /**
     * Helper method to upload image and add to DB record a photo associated to this
     * trip
     * Assumes that file and tripEntity are objects that have already been validated
     * for this function
     * 
     * @param file
     * @param tripEntity
     * @return
     * @throws IOException
     * @throws SdkClientException
     * @throws AwsServiceException
     * @throws S3Exception
     */
    private PhotoEntity uploadPhotoAddRecordTrip(MultipartFile file, TripEntity tripEntity)
            throws S3Exception, AwsServiceException, SdkClientException, IOException {
        String s3Bucket = configService.getS3BucketName();
        String key = String.format("trip_%s_%s.%s", tripEntity.getId(), UUID.randomUUID(),
                file.getContentType().split("/")[1]);

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

        return photoEntity;
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

        Date from_date = timestampToDate(tripDTO.getFrom());
        trip.setFromTime(new Timestamp(from_date.getTime()));

        Date to_date = timestampToDate(tripDTO.getTo());
        trip.setToTime(new Timestamp(to_date.getTime()));

        // tripRepository.save(trip);

        trip = tripRepository.save(trip);

        eventService.createEvents(tripDTO.getEvents(), trip);
        if (tripDTO.getPhotos() != null) {
            photoService.createPhotos(tripDTO.getPhotos(), trip);
        }

        return trip;
    }

    public TripEntity save(TripEntity t) {
        return tripRepository.save(t);
    }
}
