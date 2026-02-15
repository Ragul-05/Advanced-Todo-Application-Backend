package com.example.todoapp.service;

import com.example.todoapp.dto.ProfileResponse;
import com.example.todoapp.dto.ProfileUpdateRequest;
import com.example.todoapp.exception.BadRequestException;
import com.example.todoapp.exception.InternalServerException;
import com.example.todoapp.exception.NotFoundException;
import com.example.todoapp.model.User;
import com.example.todoapp.model.UserProfile;
import com.example.todoapp.repository.UserProfileRepository;
import com.example.todoapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ProfileService {

    private static final Logger log = LoggerFactory.getLogger(ProfileService.class);

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
        if (user == null) throw new BadRequestException("User must be provided");
        UserProfile profile = getOrCreateProfile(user);

        ProfileResponse res = new ProfileResponse();
        res.setFullName(user.getFullName());
        res.setEmail(user.getEmail());
        res.setProfileImage(profile.getImageUrl());

        return res;
    }

    /* UPDATE NAME & EMAIL */
    public ProfileResponse updateProfile(User user, ProfileUpdateRequest request) {
        if (user == null) throw new BadRequestException("User must be provided");
        if (request == null) throw new BadRequestException("Request body is missing");

        log.debug("Updating profile for user id={}: fullName={}", user.getId(), request.getFullName());

        user.setFullName(request.getFullName());
        userRepository.save(user);

        // Profile may or may not exist
        UserProfile profile = profileRepository
                .findByUser(user)
                .orElseGet(() -> getOrCreateProfile(user));

        ProfileResponse response = new ProfileResponse();
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail()); //  unchanged
        response.setProfileImage(profile.getImageUrl());

        return response;
    }


    /* UPLOAD IMAGE */
    public ProfileResponse uploadProfileImage(User user, MultipartFile file) {
        if (user == null) throw new BadRequestException("User must be provided");
        if (file == null || file.isEmpty()) throw new BadRequestException("File must be provided");

        UserProfile profile = getOrCreateProfile(user);

        try {
            // delete old image if exists
            if (profile.getPublicId() != null) {
                try {
                    cloudinaryService.deleteImage(profile.getPublicId());
                } catch (Exception e) {
                    // Log but allow upload to continue; deletion failure shouldn't block upload
                    log.warn("Failed to delete existing image for user id={} publicId={}: {}", user.getId(), profile.getPublicId(), e.getMessage());
                }
            }

            var uploadResult = cloudinaryService.uploadImage(file);

            if (uploadResult == null || uploadResult.get("url") == null) {
                throw new InternalServerException("Cloudinary returned empty upload result");
            }

            profile.setImageUrl(uploadResult.get("url"));
            profile.setPublicId(uploadResult.get("publicId"));

            profileRepository.save(profile);

            return getProfile(user);
        } catch (BadRequestException | NotFoundException e) {
            throw e; // rethrow known exceptions
        } catch (Exception e) {
            log.error("Failed to upload profile image for user id={}", user.getId(), e);
            throw new InternalServerException("Failed to upload profile image");
        }
    }

    /* DELETE IMAGE */
    public void deleteProfileImage(User user) {
        if (user == null) throw new BadRequestException("User must be provided");

        UserProfile profile = getOrCreateProfile(user);

        if (profile.getPublicId() == null) {
            log.debug("No profile image to delete for user id={}", user.getId());
            throw new NotFoundException("No profile image to delete");
        }

        try {
            cloudinaryService.deleteImage(profile.getPublicId());
        } catch (Exception e) {
            log.warn("Failed to delete image in cloudinary for user id={}: {}", user.getId(), e.getMessage());
            throw new InternalServerException("Failed to delete profile image");
        }

        profile.setImageUrl(null);
        profile.setPublicId(null);

        profileRepository.save(profile);
    }

    private UserProfile getOrCreateProfile(User user) {
        if (user == null) throw new BadRequestException("User must be provided");

        // Prefer lookup by id to avoid issues with detached/proxy User objects
        if (user.getId() != null) {
            return profileRepository.findByUserId(user.getId())
                    .or(() -> profileRepository.findByUser(user))
                    .orElseGet(() -> {
                        UserProfile p = new UserProfile();
                        p.setUser(user);
                        return profileRepository.save(p);
                    });
        }

        // If user id is null, try by user object or create
        return profileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    return profileRepository.save(p);
                });
    }
}
