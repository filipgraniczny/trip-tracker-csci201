package com.csci201finalproject.triptracker.controllers;

import com.csci201finalproject.triptracker.dtos.auth.LoginDTO;
import com.csci201finalproject.triptracker.dtos.auth.RegisterDTO;
import com.csci201finalproject.triptracker.entities.UserEntity;
import com.csci201finalproject.triptracker.interfaces.ErrorResponseClass;
import com.csci201finalproject.triptracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/trips")
public class TripController {

}
