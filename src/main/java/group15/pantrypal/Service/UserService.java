package group15.pantrypal.Service;


import group15.pantrypal.model.user;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.Boolean.TRUE;

@Service
public class UserService {

    // Autowire the repository
    @Autowired
    private UserRepository userRepository;

    // Create a new user
    public User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setIs_admin(TRUE);
        return userRepository.save(user);
    }

    // Find a user by username
    public Optional<user> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Delete a user
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}