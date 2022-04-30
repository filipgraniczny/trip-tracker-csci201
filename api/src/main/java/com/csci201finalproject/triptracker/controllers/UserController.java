package com.csci201finalproject.triptracker.controllers;

import java.util.Objects;
import java.util.Optional;

import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.interfaces.ErrorResponseClass;
import com.csci201finalproject.triptracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public @ResponseBody Object getUserById(@PathVariable Integer id) {
        UserEntity user = userService.findUserById(id);
        if (Objects.isNull(user)) {
            ErrorResponseClass errorResponse = new ErrorResponseClass(false, "NOT_FOUND", "No user found with this ID");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return user;
    }
}