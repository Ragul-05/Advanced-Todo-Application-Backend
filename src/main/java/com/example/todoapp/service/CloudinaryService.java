package com.example.todoapp.service;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /* Upload Image */
    public Map<String, String> uploadImage(MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "todoapp/profile")
            );

            return Map.of(
                    "url", result.get("secure_url").toString(),
                    "publicId", result.get("public_id").toString()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image");
        }
    }

    /* Delete Image */
    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image");
        }
    }
}

