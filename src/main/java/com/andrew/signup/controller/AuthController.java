package com.andrew.signup.controller;

import com.andrew.signup.model.User;
import com.andrew.signup.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public User register(@RequestBody SignupRequest request){
        User registeredUser = userService.registerUser(request.getEmail(), request.getPassword(),"local");
        if (registeredUser == null){

            throw new RuntimeException("User already exists");
        }
        return registeredUser;
    }
    // Simple DTO for sign-up request
    public static class SignupRequest {
        private String email;
        private String password;

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
// getters/setters...
    }

    @PostMapping("/google")
    public User googleLogin(@RequestBody GoogleLoginRequest request) {
        // 1. Verify the token with Google's libraries or endpoints
        //    Below is a simplified snippet using Google's libraries:

        String googleClientId = "902174840371-to18pb5c7dcva5uk1og5t3kl77d44jcl.apps.googleusercontent.com"; // store in env variable in real usage

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(request.getIdToken());
        } catch (Exception e) {
            throw new RuntimeException("Error verifying Google token", e);
        }

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleId = payload.getSubject();

            // 2. Check if user exists
            User user = userService.findByEmail(email);
            if (user == null) {
                // Create new user with provider="google"
                user = new User();
                user.setEmail(email);
                user.setProvider("google");
                user.setGoogleId(googleId);
                userService.save(user);
            }
            // 3. Return user or generate your own token (JWT, session cookie, etc.)
            return user;
        } else {
            throw new RuntimeException("Invalid Google token");
        }
    }

    // DTO for the Google login request
    public static class GoogleLoginRequest {
        private String idToken;
        // getter/setter

        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }


}

