package com.csci201finalproject.triptracker.services;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import com.csci201finalproject.triptracker.dtos.auth.RegisterDTO;
import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CUSTOMER_SERVICE")
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Creates new user account and returns the user non-credentials
     * 
     * @param userData - `RegisterDTO` instance; has email, password, and name
     * @return user entity
     */
    public UserEntity createUser(RegisterDTO userData) {
        UserEntity user = new UserEntity();
        user.setEmail(userData.getEmail());
        user.setName(userData.getName());
        user.setPassword(userData.getPassword());
        user = userRepository.save(user);
        user.setPassword(null); // protects credential field
        return user;
    }

    /**
     * Verifies the user email or password and returns the User object with no
     * credentials e.g password
     * 
     * @param email
     * @param password
     * @return null if email or password is invalid else returns the obfuscated user
     *         entity
     */
    public UserEntity verifyUserByEmailAndPassword(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        if (!Objects.isNull(user) && user.getPassword().equals(password)) {
            user.setPassword(null); // obscure this credential field
            return user;
        }
        return null;
    }

    public UserEntity findUserById(Integer id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty())
            return null;
        try {
            user.get().setPassword(null);
            return user.get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
