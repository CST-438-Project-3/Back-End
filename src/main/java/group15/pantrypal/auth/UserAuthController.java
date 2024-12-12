package group15.pantrypal.auth;


import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserAuthController {

    private final UserService userService;

    public UserAuthController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public void home(HttpServletResponse response) throws IOException {
         response.sendRedirect("https://http://localhost:8081/");
    };

    @GetMapping("/register")
    public void registerPageRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8081/SignUp");
    }

    // Manual registration
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> user) {
        String name = user.get("name");
        String username = user.get("username");
        String password = user.get("password");

        try {
            System.out.println("Registering user: " + username);
            userService.createUser(name, username, password, "USER"); // Default role is USER
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
    @GetMapping("/login")
    public void loginPageRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:8081/logIn"); // Redirect to your React login page
    }
    
    // Handle manual login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> user, HttpSession session) {
      String username = user.get("username");
      String password = user.get("password");
      Optional<UserAuth> userAuth = userService.findByUsername(username);
       if (userAuth.isPresent() && userService.passwordMatch(password, userAuth.get().getPassword())) {
        session.setAttribute("userId", userAuth.get().getUserId());
        session.setAttribute("username", username);
        session.setAttribute("role", userAuth.get().getRole());
        return ResponseEntity.ok(String.valueOf(userAuth.get().getUserId()));
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
    public ResponseEntity<String> oauth2Success(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("OAuth2 Principal: " + principal);

        if (principal instanceof OAuth2User oauthUser) {
            System.out.println("OAuth2 Attributes: " + oauthUser.getAttributes());

            String email = oauthUser.getAttribute("email");
            String name = oauthUser.getAttribute("name");

            UserAuth userAuth = userService.createOrUpdateOAuth2User(email, name);
            HttpSession session = request.getSession();
            session.setAttribute("username", userAuth.getUsername());
            session.setAttribute("role", userAuth.getRole());
            session.setAttribute("userId", userAuth.getUserId());
            System.out.println("userId: "+userAuth.getUserId());
            response.sendRedirect("http://localhost:8081/(tabs)");
            return ResponseEntity.ok(String.valueOf(userAuth.getUserId()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated via OAuth2.");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            System.out.println("Session not found.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in.");
        }

        System.out.println("Session ID: " + session.getId());
        session.getAttributeNames().asIterator().forEachRemaining(
                attr -> System.out.println(attr + ": " + session.getAttribute(attr))
        );

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in.");
        }

        Optional<UserAuth> user = userService.findById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
