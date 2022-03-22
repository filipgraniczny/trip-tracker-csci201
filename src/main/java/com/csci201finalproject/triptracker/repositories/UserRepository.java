package com.csci201finalproject.triptracker.repositories;

import com.csci201finalproject.triptracker.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
