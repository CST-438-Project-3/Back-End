package group15.pantrypal.Service;

import group15.pantrypal.model.User;
import group15.pantrypal.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Create a new user with all fields
    public void createUser(String username, String password, Boolean isAdmin) {
        // Validate input
        if (username == null || password == null || isAdmin == null) {
            throw new ValidationException("Username, password, and isAdmin must not be null");
        }

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new ValidationException("User with username " + username + " already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Encrypt password
        user.setIsAdmin(isAdmin != null ? isAdmin : false); // Default to false if null
        user.setEmail(username + "@example.com"); // Set default email

        // Save user to the database
        userRepository.save(user);
        System.out.println("User successfully created: " + username);
    }
    public void createUser(String username, String password) {
        createUser(username, password, false); // Default to isAdmin = false
    }

    // Find a user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Delete a user by object
    public void deleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        userRepository.delete(user);
    }

    // Delete a user by ID
    public void deleteUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        userRepository.deleteById(userId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}
