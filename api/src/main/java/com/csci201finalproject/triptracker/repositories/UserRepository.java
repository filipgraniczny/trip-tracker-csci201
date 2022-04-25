package com.csci201finalproject.triptracker.repositories;

import com.csci201finalproject.triptracker.entities.UserEntity;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);
}
