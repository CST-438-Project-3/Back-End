package group15.pantrypal.Controllers;

import group15.pantrypal.Service.UserService;
import group15.pantrypal.model.user;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/newuser")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
        System.out.println("Received POST request for /newuser");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        userService.createUser(username, password);
        return ResponseEntity.ok("Success! Account created! Welcome, " + username + "!");
    }

    @GetMapping("/newuser")
    public ResponseEntity<String> getNewUserForm() {
        return ResponseEntity.ok("Please use a POST request to create a user.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Optional<user> user = userService.findByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("username", username);
            return ResponseEntity.ok("Welcome, " + username + "!");
        }

        return ResponseEntity.status(401).body("Invalid credentials.");
    }

    @GetMapping("/")
    public String home() {
        return "This is the root! Woohoo!!";
    }

    @GetMapping("/error")
    public String error() {
        return "An error occurred. Please try again.";
    }
}