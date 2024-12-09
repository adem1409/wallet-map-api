package com.walletmap.api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PathVariable;
=======
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
>>>>>>> b6b321a65e27583033e1f4958a7f88d47b8386e4
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
<<<<<<< HEAD
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import com.walletmap.api.models.User;
import com.walletmap.api.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
=======
import org.springframework.web.multipart.MultipartFile;

import com.walletmap.api.lib.AuthHelpers;
import com.walletmap.api.lib.FileManager;
import com.walletmap.api.models.User;
import com.walletmap.api.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
>>>>>>> b6b321a65e27583033e1f4958a7f88d47b8386e4

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

    @PostMapping("/edit/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody Map<String, String> updatedUser) {
        System.out.println(updatedUser);
        Optional<User> editedUser = userService.editUser(id, updatedUser);

        if (editedUser.isPresent()) {
            return ResponseEntity.ok(editedUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        try {

            if (userService.emailExists(user.getEmail())) {
                return new ResponseEntity<>(Map.of("message", "EmailAlreadyExists"), HttpStatus.BAD_REQUEST);
            }
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setAccessId(1);
            User savedUser = userService.saveUser(newUser);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test")
    public String test(HttpServletRequest req) {
        String path = System.getProperty("user.dir") + "/" + "test.txt";
        System.out.println(path);
        System.out.println("user -----------" + req.getAttribute("currentUser").toString());
        return new String();
    }

    @Autowired
    private AuthHelpers authHelpers;

    @PostMapping("/update-picture")
    public ResponseEntity<?> updatePicture(
            @RequestParam("picture") MultipartFile file,
            HttpServletRequest request) {
        try {
            // Fetch the authenticated user from request
            // Optional<User> currentUserOptional = (Optional<User>)
            // request.getAttribute("currentUser");
            User currentUser = authHelpers.getAuthenticatedUser(request);

            // if (currentUserOptional == null || currentUserOptional.isEmpty()) {
            // return ResponseEntity.status(401).body("Not authenticated");
            // }

            if (currentUser == null) {
                return ResponseEntity.status(401).body("Not authenticated");
            }

            // Get the User object from Optional
            // User currentUser = currentUserOptional.get();

            // Define the upload directory (e.g., "uploads/photos/")
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

}
