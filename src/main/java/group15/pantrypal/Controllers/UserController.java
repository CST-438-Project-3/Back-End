package group15.pantrypal.Controllers;

import group15.pantrypal.Service.UserService;
import group15.pantrypal.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password) {
        userService.createUser(username, password, false); // Assuming default `isAdmin` is false
        return ResponseEntity.ok("Account created successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("username", username);
            return ResponseEntity.ok("Login successful. Welcome, " + username + "!");
        }

        return ResponseEntity.status(401).body("Invalid credentials.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PatchMapping("/users/profile")
    public ResponseEntity<String> updateProfile(@RequestParam String username, @RequestParam String email, HttpSession session) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername == null) {
            return ResponseEntity.status(403).body("You are not logged in.");
        }
        userService.findByUsername(currentUsername).ifPresent(user -> {
            user.setUsername(username);
            user.setEmail(email);
            userService.createUser(user.getUsername(), user.getPassword(), user.getIsAdmin());
        });
        return ResponseEntity.ok("Profile updated successfully.");
    }

    @DeleteMapping("/users/account")
    public ResponseEntity<String> deleteAccount(HttpSession session) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername == null) {
            return ResponseEntity.status(403).body("You are not logged in.");
        }

        Optional<User> user = userService.findByUsername(currentUsername);
        user.ifPresent(userService::deleteUser);
        session.invalidate();
        return ResponseEntity.ok("Account deleted successfully.");
    }
}
