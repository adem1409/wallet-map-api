package com.walletmap.api.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.walletmap.api.lib.Helpers;
import com.walletmap.api.models.User;
import com.walletmap.api.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Get the JWT from the "auth-token" cookie
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

                // Fetch the user and store in the request attribute
                Optional<User> userOptional = userService.findById(Long.parseLong(userId));
                if (userOptional.isPresent()) {
                    request.setAttribute("currentUser", userOptional);
                    System.out.println(userOptional);
                }
                ;
            } catch (Exception e) {
                // Log exception or handle invalid token
            }
        }

        return true; // Continue with the request
    }
}
