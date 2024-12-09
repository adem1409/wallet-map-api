package com.walletmap.api.lib;

import com.walletmap.api.models.User;
import com.walletmap.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthHelpers {

    @Autowired
    private UserRepository userRepository;

    // Method to get the authenticated user
    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("auth-token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                // Parse the JWT to get the user ID
                String userId = Helpers.parseJWT(token);

                // Fetch the user and return it
                return userRepository.findById(Long.parseLong(userId))
                        .orElse(null); // Return null if user not found
            } catch (Exception e) {
                // Handle error if JWT parsing fails or any other issue occurs
                return null;
            }
        }
        return null;
    }
}
