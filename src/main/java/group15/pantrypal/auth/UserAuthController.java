package group15.pantrypal.auth;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    private final UserService userService;

    public UserAuthController(UserService userService) {
        this.userService = userService;
    }

    // Manual registration
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        try {
            System.out.println("Registering user: " + username);
            userService.createUser(username, password, "USER"); // Default role is USER
            System.out.println("User registered successfully: " + username);
            return ResponseEntity.ok("Account created successfully!");
        } catch (UserService.ValidationException e) {
            System.err.println("Validation error during registration: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Log full stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration.");
        }
    }


    // Manual login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Optional<UserAuth> user = userService.findByUsername(username);
        if (user.isPresent() && userService.passwordMatch(password, user.get().getPassword())) {
            session.setAttribute("username", username);
            session.setAttribute("role", user.get().getRole());
            return ResponseEntity.ok("Login successful. Welcome, " + username + "!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    // OAuth2 success handler
    @GetMapping("/oauth2-success")
    public ResponseEntity<String> oauth2Success(HttpSession session) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("OAuth2 Principal: " + principal);

        if (principal instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) principal;
            System.out.println("OAuth2 Attributes: " + oauthUser.getAttributes());

            String email = oauthUser.getAttribute("email");
            String name = oauthUser.getAttribute("name");

            UserAuth userAuth = userService.createOrUpdateOAuth2User(email, name);
            session.setAttribute("username", userAuth.getUsername());
            session.setAttribute("role", userAuth.getRole());
            return ResponseEntity.ok("OAuth2 login successful. Welcome, " + name + "!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated via OAuth2.");
    }

    // OAuth2 failure handler
    @GetMapping("/oauth2-failure")
    public ResponseEntity<String> oauth2Failure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 login failed. Please try again.");
    }

    // Update profile
    @PatchMapping("/users/profile")
    public ResponseEntity<String> updateProfile(@RequestParam String username, @RequestParam String email, HttpSession session) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not logged in.");
        }
        userService.findByUsername(currentUsername).ifPresent(userAuth -> {
            userAuth.setUsername(username);
            userAuth.setEmail(email);
            userService.saveUser(userAuth);
        });
        return ResponseEntity.ok("Profile updated successfully.");
    }

    // Delete account
    @DeleteMapping("/users/account")
    public ResponseEntity<String> deleteAccount(HttpSession session) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not logged in.");
        }

        Optional<UserAuth> user = userService.findByUsername(currentUsername);
        user.ifPresent(userService::deleteUser);
        session.invalidate();
        return ResponseEntity.ok("Account deleted successfully.");
    }

    // Global exception handler
    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(UserService.ValidationException.class)
        public ResponseEntity<String> handleValidationException(UserService.ValidationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGeneralException(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
