package group15.pantrypal.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to PantryPal! This is the root.";
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.status(500).body("An error occurred. Please try again.");
    }
}
