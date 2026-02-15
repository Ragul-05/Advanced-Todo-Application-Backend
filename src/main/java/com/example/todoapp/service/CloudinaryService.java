package com.example.todoapp.service;

import com.cloudinary.Cloudinary;
import com.example.todoapp.exception.InternalServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /* Upload Image */
    public Map<String, String> uploadImage(MultipartFile file) {
        try {
            var bytes = file.getBytes();
            Map<?, ?> result = cloudinary.uploader().upload(
                    bytes,
                    Map.of("folder", "todoapp/profile")
            );

            return Map.of(
                    "url", result.get("secure_url").toString(),
                    "publicId", result.get("public_id").toString()
            );

        } catch (Exception e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new InternalServerException("Failed to upload image");
        }
    }

    /* Delete Image */
    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (Exception e) {
            log.warn("Failed to delete Cloudinary image (publicId={}), continuing: {}", publicId, e.getMessage());
            // Wrap as internal server exception so handler returns 500 if callers choose to propagate
            throw new InternalServerException("Failed to delete image");
        }
    }
}
