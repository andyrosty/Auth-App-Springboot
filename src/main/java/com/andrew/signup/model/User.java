package com.andrew.signup.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")

public class User {
    @Id
    private String id;

    private String email;

    // For local sign-ups, we store a hashed password.
    private String password;

    // For Google or other social providers, keep track of provider details
    private String provider; // e.g., "local" or "google"
    private String googleId; // store the Google user ID if using Google sign-in

    // Constructors
    public User() {}

    public User(String email, String password, String provider) {
        this.email = email;
        this.password = password;
        this.provider = provider;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
