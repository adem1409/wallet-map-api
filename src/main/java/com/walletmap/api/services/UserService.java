package com.walletmap.api.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.walletmap.api.models.User;
import com.walletmap.api.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void removeById(Long id) {
        userRepository.deleteById(id);
    }

    // Edit an existing user
    public Optional<User> editUser(Long id, Map<String, String> updatedUser) throws Exception {

        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (!updatedUser.get("password").equals(user.getPassword())) {
                throw new Exception("Password is incorrect.");
            }

            // Update fields
            user.setUsername(updatedUser.get("username"));
            user.setPassword(updatedUser.get("newPassword"));
            // Add other fields to update as needed

            return Optional.of(userRepository.save(user));
        }

        return Optional.empty();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean emailExists(String email) {
        System.out.println(email);
        System.out.println(userRepository.findByEmail(email));
        System.out.println(userRepository.findByEmail(email).isPresent());
        return userRepository.findByEmail(email).isPresent();
    }

    public User findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }
}