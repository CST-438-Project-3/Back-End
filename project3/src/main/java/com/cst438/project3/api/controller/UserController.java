package com.cst438.project3.api.controller;

import java.util.Optional;

import com.cst438.project3.api.model.User;
import com.cst438.project3.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {

    // Dependency injection for UserService
    @Autowired
    private UserService userService;

    // Create a new user
    @PostMapping("/newuser")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
        System.out.println("Received POST request for /newuser");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        userService.createUser(username, password); // FIX: Use the injected `userService` instance
        return ResponseEntity.ok("Success! Account created! Welcome, " + username + "!");
    }

    @GetMapping("/newuser")
    public ResponseEntity<String> getNewUserForm() {
        return ResponseEntity.ok("Please use a POST request to create a user.");
    }

    // Login a user
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("username", username);
            return ResponseEntity.ok("Welcome, " + username + "!");
        }

        return ResponseEntity.status(401).body("Invalid credentials.");
    }

    // Root endpoint
    @GetMapping("/")
    public String home() {
        return "This is the root! Woohoo!!";
    }

    // Error endpoint
    @GetMapping("/error")
    public String error() {
        return "An error occurred. Please try again.";
    }
}
