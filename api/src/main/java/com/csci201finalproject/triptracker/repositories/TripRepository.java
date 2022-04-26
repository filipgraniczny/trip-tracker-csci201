package com.csci201finalproject.triptracker.repositories;

import com.csci201finalproject.triptracker.entities.TripEntity;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<TripEntity, Integer> {
}