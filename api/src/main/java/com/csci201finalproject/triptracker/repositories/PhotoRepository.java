package com.csci201finalproject.triptracker.repositories;

import com.csci201finalproject.triptracker.entities.PhotoEntity;

import org.springframework.data.repository.CrudRepository;

public interface PhotoRepository extends CrudRepository<PhotoEntity, Integer> {
}
