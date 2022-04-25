package com.csci201finalproject.triptracker.services;

import com.csci201finalproject.triptracker.dtos.auth.RegisterDTO;
import com.csci201finalproject.triptracker.entities.User;
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
    public User createUser(RegisterDTO userData) {
        User user = new User();
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
    public User verifyUserByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user.getPassword().equals(password)) {
            user.setPassword(null); // obscure this credential field
            return user;
        }
        return null;
    }
}
