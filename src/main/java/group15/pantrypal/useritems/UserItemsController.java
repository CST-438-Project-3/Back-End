package group15.pantrypal.useritems;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userItems")
public class UserItemsController {
    private final  UserItemsRepository userItemsRepository;

    public UserItemsController(UserItemsRepository userItemsRepository) {
        this.userItemsRepository = userItemsRepository;
    }

    // Create a new user item
    @PostMapping
    public ResponseEntity<UserItems> createUserItem(@RequestBody UserItems userItem) {
        UserItems savedUserItem = userItemsRepository.save(userItem);
        return new ResponseEntity<>(savedUserItem, HttpStatus.CREATED);
    }

    // Get all user items
    @GetMapping
    public ResponseEntity<List<UserItems>> getAllUserItems() {
        List<UserItems> userItems = userItemsRepository.findAll();
        return new ResponseEntity<>(userItems, HttpStatus.OK);
    }

    // Get all items by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserItems>> getUserItemsByUserId(@PathVariable Long userId) {
        List<UserItems> userItems = userItemsRepository.findByUserId(userId);
        return new ResponseEntity<>(userItems, HttpStatus.OK);
    }

    // Get a user item by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserItems> getUserItemById(@PathVariable Long id) {
        return userItemsRepository.findById(id)
                .map(userItem -> new ResponseEntity<>(userItem, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a user item
    @PutMapping("/{id}")
    public ResponseEntity<UserItems> updateUserItem(@PathVariable Long id, @RequestBody UserItems userItemDetails) {
        return userItemsRepository.findById(id)
                .map(userItem -> {
                    userItem.setItemId(userItemDetails.getItemId());
                    userItem.setUserId(userItemDetails.getUserId());
                    userItem.setQuantity(userItemDetails.getQuantity());
                    userItem.setIsFavorite(userItemDetails.getIsFavorite());
                    UserItems updatedUserItem = userItemsRepository.save(userItem);
                    return new ResponseEntity<>(updatedUserItem, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a user item partially
    @PatchMapping("/{id}")
    public ResponseEntity<UserItems> updateUserItemPartial(@PathVariable Long id, @RequestBody UserItems userItemDetails) {
        return userItemsRepository.findById(id)
                .map(userItem -> {
                    if (userItemDetails.getItemId() != null) {
                        userItem.setItemId(userItemDetails.getItemId());
                    }
                    if (userItemDetails.getUserId() != null) {
                        userItem.setUserId(userItemDetails.getUserId());
                    }
                    if (userItemDetails.getExpirationDate() != null) {
                        userItem.setExpirationDate(userItemDetails.getExpirationDate());
                    }
                    if (userItemDetails.getUnit() != null) {
                        userItem.setUnit(userItemDetails.getUnit());
                    }
                    if (userItemDetails.getIsFavorite() != userItem.getIsFavorite()) {
                        userItem.setIsFavorite(userItemDetails.getIsFavorite());
                    }
                    UserItems updatedUserItem = userItemsRepository.save(userItem);
                    return new ResponseEntity<>(updatedUserItem, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete a user item
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserItem(@PathVariable Long id) {
        return userItemsRepository.findById(id)
                .map(userItem -> {
                    userItemsRepository.delete(userItem);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
