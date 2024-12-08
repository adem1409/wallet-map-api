package com.walletmap.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.walletmap.api.models.User;
import com.walletmap.api.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String get() {
        return "test test";
    }

    @GetMapping("/all")
    public String getAllUsers() {
        return userService.getAllUsers().toString();
    }

    // @PostMapping
    // public String createUser(@RequestBody User user) {
    // User entity;
    // try {
    // entity = userService.saveUser(user);

    // } catch (Exception e) {
    // return e.getMessage();
    // }
    // return user.toString();
    // }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

}
