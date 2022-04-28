package com.csci201finalproject.triptracker.repositories;

import com.csci201finalproject.triptracker.entities.TripEntity;
import com.csci201finalproject.triptracker.entities.UserEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TripRepository extends CrudRepository<TripEntity, Integer> {
    List<TripEntity> findAllByTitleContaining(String searchTerm, Pageable pageRequest);
}