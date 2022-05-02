package com.csci201finalproject.triptracker.repositories;

import java.util.Optional;

import com.csci201finalproject.triptracker.entities.PhotoEntity;
import com.csci201finalproject.triptracker.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);

    UserEntity findByProfilePhotoEntity(PhotoEntity photoEntity);
}
