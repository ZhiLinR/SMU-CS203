package com.example.profileapi.controller;

import com.example.profileapi.model.ProfileRequest;
import com.example.profileapi.service.ProfileService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // Sample Request Body
    // {
    //     "email": "user@example.com",
    //     "password": "hashed_password_here",
    //     "isAdmin": 1
    // }
    @PostMapping("/profile")
    public ResponseEntity<?> createProfile(@RequestBody ProfileRequest profileRequest) {
        return profileService.createProfile(profileRequest);
    }

    // Sample Request Body
    // {
    //     "email": "user@example.com",
    //     "password": "hashed_password_here"
    // }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ProfileRequest loginRequest) {
        return profileService.authenticateUser(loginRequest);
    }


    @GetMapping("/profile/{uuid}")
    public ResponseEntity<?> getProfileByUid(@PathVariable("uuid") String uuid) {
        return profileService.getProfileByUUID(uuid);
    }
}
