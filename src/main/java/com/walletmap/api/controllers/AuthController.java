package com.walletmap.api.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walletmap.api.lib.Helpers;
import com.walletmap.api.models.User;
import com.walletmap.api.repositories.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest,
            HttpServletResponse response) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        // Check if email exists and password matches
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(password)) {
            // Generate JWT token with user ID
            String jwtToken = Helpers.generateJWT(userOptional.get().getId().toString());

            // Create HTTP-only cookie with the token
            ResponseCookie jwtCookie = ResponseCookie.from("auth-token", jwtToken)
                    .httpOnly(true)
                    .sameSite("None")
                    .secure(true)
                    .path("/")
                    .build();

            // Add the cookie to the response header
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            return ResponseEntity.ok("Login successful");
        } else {
            // Return unauthorized if credentials are invalid
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Create an expired cookie to remove the JWT
        ResponseCookie jwtCookie = ResponseCookie.from("auth-token", "")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        // Add the expired cookie to the response header
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return ResponseEntity.ok("Logout successful");
    }

    // Token verification endpoint
    @GetMapping("/status")
    public ResponseEntity<?> verify(@CookieValue(name = "auth-token", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        try {
            // Parse JWT and extract user ID
            String userId = Helpers.parseJWT(token);

            System.out.println("User ID: " + userId);

            // Fetch user by ID
            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional);
            } else {
                return ResponseEntity.status(401).body("Invalid or expired token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }
}
