package com.example.todoapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String publicId; // 👈 REQUIRED for Cloudinary delete

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /* Getters & Setters */

    public Long getId() { return id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}

