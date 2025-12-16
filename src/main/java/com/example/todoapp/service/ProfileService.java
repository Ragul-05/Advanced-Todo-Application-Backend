package com.example.todoapp.service;

import com.example.todoapp.dto.ProfileResponse;
import com.example.todoapp.dto.ProfileUpdateRequest;
import com.example.todoapp.model.User;
import com.example.todoapp.model.UserProfile;
import com.example.todoapp.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ProfileService {

    private final UserProfileRepository profileRepository;
    private final CloudinaryService cloudinaryService;

    public ProfileService(UserProfileRepository profileRepository,
                          CloudinaryService cloudinaryService) {
        this.profileRepository = profileRepository;
        this.cloudinaryService = cloudinaryService;
    }

    /* GET PROFILE */
    public ProfileResponse getProfile(User user) {
        UserProfile profile = getOrCreateProfile(user);

        ProfileResponse res = new ProfileResponse();
        res.setFullName(user.getFullName());
        res.setEmail(user.getEmail());
        res.setProfileImage(profile.getImageUrl());

        return res;
    }

    /* UPDATE NAME & EMAIL */
    public ProfileResponse updateProfile(User user, ProfileUpdateRequest request) {

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        return getProfile(user);
    }

    /* UPLOAD IMAGE */
    public ProfileResponse uploadProfileImage(User user, MultipartFile file) {

        UserProfile profile = getOrCreateProfile(user);

        // delete old image if exists
        if (profile.getPublicId() != null) {
            cloudinaryService.deleteImage(profile.getPublicId());
        }

        var uploadResult = cloudinaryService.uploadImage(file);

        profile.setImageUrl(uploadResult.get("url"));
        profile.setPublicId(uploadResult.get("publicId"));

        profileRepository.save(profile);

        return getProfile(user);
    }

    /* DELETE IMAGE */
    public void deleteProfileImage(User user) {

        UserProfile profile = getOrCreateProfile(user);

        if (profile.getPublicId() != null) {
            cloudinaryService.deleteImage(profile.getPublicId());
        }

        profile.setImageUrl(null);
        profile.setPublicId(null);

        profileRepository.save(profile);
    }

    private UserProfile getOrCreateProfile(User user) {
        return profileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    return profileRepository.save(p);
                });
    }
}
