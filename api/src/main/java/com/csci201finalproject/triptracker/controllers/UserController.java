package com.csci201finalproject.triptracker.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.csci201finalproject.triptracker.entities.PhotoEntity;
import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.interfaces.ErrorResponseClass;
import com.csci201finalproject.triptracker.services.UserService;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/{id}/pfp")
    public @ResponseBody ResponseEntity<Object> updateUserProfilePhoto(
            @RequestParam(name = "file", required = true) MultipartFile multipart,
            @PathVariable(name = "id") Integer id) {
        try {
            UserEntity user = userService.findUserById(id);
            List<Object> resultList = userService.updateProfileImageUser(user, multipart);
            PhotoEntity photoEntity = (PhotoEntity) resultList.get(1);
            return ResponseEntity.ok().body(photoEntity);
        } catch (IllegalArgumentException | IOException exception) {
            ErrorResponseClass error = new ErrorResponseClass(false, "INVALID_ARGUMENT", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}