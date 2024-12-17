package com.walletmap.api.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walletmap.api.models.User;
import com.walletmap.api.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAll(String search) {
        if (search != null && !search.isBlank()) {
            return userRepository.findByEmailContainingIgnoreCaseOrUsernameContainingIgnoreCase(search, search);
        }
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

            // if (!updatedUser.get("password").equals(user.getPassword())) {
            // throw new Exception("Password is incorrect.");
            // }

            // Update fields
            user.setUsername(updatedUser.get("username"));
            // user.setPassword(updatedUser.get("newPassword"));
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

    public Optional<User> findByEmail(String email) {
        // TODO Auto-generated method stub
        return userRepository.findByEmail(email);
    }
}