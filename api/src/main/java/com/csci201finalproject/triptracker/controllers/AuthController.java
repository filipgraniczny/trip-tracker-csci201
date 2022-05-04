package com.csci201finalproject.triptracker.controllers;

import java.util.Objects;

import com.csci201finalproject.triptracker.dtos.auth.LoginDTO;
import com.csci201finalproject.triptracker.dtos.auth.RegisterDTO;
import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.interfaces.ErrorResponseClass;
import com.csci201finalproject.triptracker.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public @ResponseBody Object register(@RequestBody RegisterDTO registerDTO) {
        UserEntity resultUser;
        try {
            resultUser = userService.createUser(registerDTO);
            return resultUser;
        } catch (Exception e) { // TODO: find out what this error class is exactly
            ErrorResponseClass error = new ErrorResponseClass(false, "USER_REGISTERED",
                    "User under this email has already been registered");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    @PostMapping("/login")
    public @ResponseBody Object login(@RequestBody LoginDTO loginDTO) {
        UserEntity user = userService.verifyUserByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if (Objects.isNull(user)) {
            ErrorResponseClass error = new ErrorResponseClass(false, "INVALID_LOGIN", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return user;
    }
}