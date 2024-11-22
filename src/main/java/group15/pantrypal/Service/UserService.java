package group15.pantrypal.Service;

import group15.pantrypal.model.User;
import group15.pantrypal.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor injection (preferred)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create a new user with isAdmin parameter
    public void createUser(String username, String password, Boolean isAdmin) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setIsAdmin(isAdmin);
        userRepository.save(user);
    }

    // Overloaded method to create a new user (default to non-admin)
    public void createUser(String username, String password) {
        createUser(username, password, false); // Default to isAdmin = false
    }

    // Find a user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Delete a user by object
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    // Delete a user by ID
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
