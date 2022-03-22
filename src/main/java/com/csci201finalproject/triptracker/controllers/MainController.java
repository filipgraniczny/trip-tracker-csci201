package com.csci201finalproject.triptracker.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class MainController {
    @GetMapping(path = "/")
    public @ResponseBody HashMap<String, String> hello() {
        HashMap<String, String> response = new HashMap<>();
        response.put("code", "200");
        response.put("data", "hello world");
        return response;
    }
}
