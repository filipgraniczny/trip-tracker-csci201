package com.csci201finalproject.triptracker.controllers;

import com.csci201finalproject.triptracker.dtos.auth.RegisterDTO;
import com.csci201finalproject.triptracker.entities.User;
import com.csci201finalproject.triptracker.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public @ResponseBody User register(@RequestBody RegisterDTO registerDTO) {
        User resultUser = userService.createUser(registerDTO);
        return resultUser;
    }

    @GetMapping("/login")
    public @ResponseBody User login(@RequestParam String email, @RequestParam String password) {
        return userService.verifyUserByEmailAndPassword(email, password);
    }
}
