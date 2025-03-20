package com.andrew.signup.service;

import com.andrew.signup.model.User;
import com.andrew.signup.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //Password Hashing
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // REGISTER (Email/Password)
    public User registerUser(String email, String plainPassword, String provider) {
        //Check if user exist
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return null;
        }

        //encode with bcrypt beofre storing
        String hashedPassword = passwordEncoder.encode(plainPassword);

        User newUser = new User(email, hashedPassword, provider);
        return userRepository.save(newUser);
    }
    // FIND BY EMAIL
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // SAVE (useful for Google sign-in or updating user details)
    public User save(User user) {
        return userRepository.save(user);
    }
}

