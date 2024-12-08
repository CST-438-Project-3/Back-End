package group15.pantrypal;

import group15.pantrypal.Model.User;
import group15.pantrypal.UserRepository;
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

    // Create a user for manual registration
    public void createUser(String username, String password, String role) {
        System.out.println("Creating user: " + username);
        validateUserInput(username, password);

        if (findByUsername(username).isPresent()) {
            System.err.println("User already exists: " + username);
            throw new ValidationException("User already exists.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Encrypt password
        user.setRole(role); // Set role
        user.setEmail(username + "@example.com");

        userRepository.save(user);
        System.out.println("User saved: " + username);
    }

    // Overloaded createUser method for default role = "USER"
    public void createUser(String username, String password) {
        createUser(username, password, "USER");
    }

    // Create or update a user for OAuth2 login
    public User createOrUpdateOAuth2User(String email, String name) {
        // Check if user already exists
        Optional<User> existingUser = findByUsername(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(name); // Update the name if needed
            return saveUser(user);
        } else {
            // Create a new user
            User user = new User();
            user.setUsername(name);
            user.setEmail(email);
            user.setRole("USER"); // Default to "USER" role for OAuth2 users
            user.setPassword("oauth2-user"); // Placeholder password (not used for OAuth2)

            return saveUser(user);
        }
    }

    // Save user to the database
    public User saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        return userRepository.save(user);
    }

    // Validate user input
    private void validateUserInput(String username, String password) {
        if (username == null || password == null) {
            throw new ValidationException("Username and password must not be null");
        }
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

    public boolean passwordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}
