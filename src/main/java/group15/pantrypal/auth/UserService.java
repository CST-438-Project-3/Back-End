package group15.pantrypal.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Service
public class UserService {

    private final UserAuthRepository userAuthRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
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

        UserAuth userAuth = new UserAuth();
        userAuth.setUsername(username);
        userAuth.setPassword(passwordEncoder.encode(password)); // Encrypt password
        userAuth.setRole(role); // Set role
        userAuth.setEmail(username + "@example.com");

        userAuthRepository.save(userAuth);
        System.out.println("User saved: " + username);
    }

    // Overloaded createUser method for default role = "USER"
    public void createUser(String username, String password) {
        createUser(username, password, "USER");
    }

    // Create or update a user for OAuth2 login
    public UserAuth createOrUpdateOAuth2User(String email, String name) {
        // Check if user already exists
        Optional<UserAuth> existingUser = findByEmail(email);

        if (existingUser.isEmpty()) {
            // Create a new user
            UserAuth userAuth = new UserAuth();
            userAuth.setUsername(name);
            userAuth.setEmail(email);
            userAuth.setRole("USER"); // Default to "USER" role for OAuth2 users
            userAuth.setPassword(null); // password not needed for OAuth2 users

            return saveUser(userAuth);
        } else {
            UserAuth userAuth = existingUser.get();
            userAuth.setUsername(existingUser.get().getUsername()); // Update the name if needed
            return saveUser(userAuth);
        }
    }

    private Optional<UserAuth> findByEmail(String email) {
        return userAuthRepository.findByEmail(email);
    }

    // Save user to the database
    public UserAuth saveUser(UserAuth userAuth) {
        if (userAuth == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        return userAuthRepository.save(userAuth);
    }

    // Validate user input
    private void validateUserInput(String username, String password) {
        if (username == null || password == null) {
            throw new ValidationException("Username and password must not be null");
        }
    }

    // Find a user by username
    public Optional<UserAuth> findByUsername(String username) {
        return userAuthRepository.findByUsername(username);
    }

    // Delete a user by object
    public void deleteUser(UserAuth userAuth) {
        if (userAuth == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        userAuthRepository.delete(userAuth);
    }

    // Delete a user by ID
    public void deleteUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        userAuthRepository.deleteById(userId);
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
