package com.hotel_booking.service;

import com.hotel_booking.model.User;
import com.hotel_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public User login(String email, String rawPassword) {
        User user = repo.findByEmail(email);
        if (user == null) return null;
        try {
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        } catch (Exception e) {
            // Stored password is not BCrypt encoded (legacy plain-text)
            // Fall back to plain-text comparison and re-encode on success
            if (rawPassword.equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(rawPassword));
                repo.save(user);
                return user;
            }
        }
        return null;
    }
}
