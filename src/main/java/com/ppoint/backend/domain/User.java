package com.ppoint.backend.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String instagramUser;

    @Column(unique = true)
    private String email;
    private String password;
    private String role;
    private String provider;
    private String googleId;
    private String picture;

    // Getters:
    public String getEmail() {
        return email;
    }
    public String getInstagramUser() {
        return instagramUser;
    }
    public String getRole() {
        return role;
    }
    public String getPassword() {
        return password;
    }
    public String getProvider() {
        return provider;
    }
    public UUID getId() {
        return id;
    }
    public String getGoogleId() {
        return googleId;
    }
    public String getPicture() {
        return picture;
    }

    // Setters:
    public void setId(UUID id) {
        this.id = id;
    }
    public void setInstagramUser(String name) {
        this.instagramUser = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
}