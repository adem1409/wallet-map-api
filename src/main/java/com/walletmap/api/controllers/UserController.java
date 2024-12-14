package com.walletmap.api.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.walletmap.api.lib.AuthHelpers;
import com.walletmap.api.lib.FileManager;
import com.walletmap.api.lib.Helpers;
import com.walletmap.api.models.User;
import com.walletmap.api.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editUser(@RequestBody Map<String, String> updatedUser,
            HttpServletRequest request) {

        try {

            User currentUser = authHelpers.getAuthenticatedUser(request);

            if (currentUser == null) {
                return ResponseEntity.status(401).body("Not authenticated");
            }

            Optional<User> editedUser = userService.editUser(currentUser.getId(), updatedUser);

            if (editedUser.isPresent()) {
                return ResponseEntity.ok(editedUser.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }
    }

    @Autowired
    private AuthHelpers authHelpers;

    @PostMapping("/update-picture")
    public ResponseEntity<?> updatePicture(
            @RequestParam("picture") MultipartFile file,
            HttpServletRequest request) {
        try {

            User currentUser = authHelpers.getAuthenticatedUser(request);

            if (currentUser == null) {
                return ResponseEntity.status(401).body("Not authenticated");
            }

            String uploadDir = "/uploads/profile-pictures/";

            // Use FilesManager to upload the file
            String uploadedFilePath = FileManager.uploadFile(file, uploadDir);
            System.out.println("--------------- uploadedFilePath");
            System.out.println(uploadedFilePath);

            // Update the user's picture field
            currentUser.setPicture(uploadedFilePath);
            userService.saveUser(currentUser);

            return ResponseEntity.ok(Map.of("message", "Photo uploaded successfully", "filePath", uploadedFilePath));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user, HttpServletResponse response) {
        try {

            if (userService.emailExists(user.getEmail())) {
                return new ResponseEntity<>(Map.of("message", "EmailAlreadyExists"), HttpStatus.BAD_REQUEST);
            }
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setAccessId(1);
            newUser.setPicture("/uploads/profile-pictures/avatar_placeholder.png");
            User savedUser = userService.saveUser(newUser);

            // Generate JWT token with user ID
            String jwtToken = Helpers.generateJWT(savedUser.getId().toString());

            // Create HTTP-only cookie with the token
            ResponseCookie jwtCookie = ResponseCookie.from("auth-token", jwtToken)
                    .httpOnly(true)
                    .sameSite("None")
                    .secure(true)
                    .path("/")
                    .build();

            // Add the cookie to the response header
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
