package com.example.todoapp.controller;

import com.example.todoapp.dto.ProfileResponse;
import com.example.todoapp.dto.ProfileUpdateRequest;
import com.example.todoapp.model.User;
import com.example.todoapp.service.ProfileService;
import com.example.todoapp.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /* GET PROFILE */
    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(
                ApiResponse.success("Profile fetched", profileService.getProfile(user))
        );
    }

    /* UPDATE NAME */
    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @RequestBody ProfileUpdateRequest request,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile updated successfully",
                        profileService.updateProfile(user, request)
                )
        );
    }

    /* UPLOAD IMAGE */
    @PostMapping("/image")
    public ResponseEntity<ApiResponse<ProfileResponse>> uploadImage(
            @RequestParam("image") MultipartFile image,
            Authentication auth) {

        User user = (User) auth.getPrincipal();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile image uploaded",
                        profileService.uploadProfileImage(user, image)
                )
        );
    }

    /* DELETE IMAGE */
    @DeleteMapping("/image")
    public ResponseEntity<ApiResponse<Void>> deleteImage(Authentication auth) {

        User user = (User) auth.getPrincipal();
        profileService.deleteProfileImage(user);

        return ResponseEntity.ok(
                ApiResponse.success("Profile image deleted", null)
        );
    }
}

