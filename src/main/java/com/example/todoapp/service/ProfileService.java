package com.example.todoapp.service;

import com.example.todoapp.dto.ProfileResponse;
import com.example.todoapp.dto.ProfileUpdateRequest;
import com.example.todoapp.model.User;
import com.example.todoapp.model.UserProfile;
import com.example.todoapp.repository.UserProfileRepository;
import com.example.todoapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ProfileService {

    private final UserProfileRepository profileRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    public ProfileService(UserProfileRepository profileRepository,
                          CloudinaryService cloudinaryService,UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
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

        user.setFullName(request.getFullName());
        userRepository.save(user);

        // Profile may or may not exist
        UserProfile profile = profileRepository
                .findByUser(user)
                .orElseGet(() -> getOrCreateProfile(user));

        ProfileResponse response = new ProfileResponse();
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail()); // 🔒 unchanged
        response.setProfileImage(profile.getImageUrl());

        return response;
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
