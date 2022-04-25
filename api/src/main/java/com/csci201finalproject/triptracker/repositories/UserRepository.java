package com.csci201finalproject.triptracker.repositories;

import java.util.List;

import com.csci201finalproject.triptracker.entities.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
