package group15.pantrypal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Create a new item
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemRepository.save(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    // Get all items
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // Get an item by ID
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update an item
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setItemName(itemDetails.getItemName());
                    item.setItemCategory(itemDetails.getItemCategory());
                    item.setItemQuantity(itemDetails.getItemQuantity());
                    item.setItemUrl(itemDetails.getItemUrl());
                    item.setIsFavorite(itemDetails.getIsFavorite());
                    Item updatedItem = itemRepository.save(item);
                    return new ResponseEntity<>(updatedItem, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    // Update an item partially
//    @PatchMapping("/{id}")
//    public ResponseEntity<Item> updateItemPartial(@PathVariable Long id, @RequestBody Item itemDetails) {
//        return itemRepository.findById(id)
//                .map(item -> {
//                    if (itemDetails.getItemName() != null) {
//                        item.setItemName(itemDetails.getItemName());
//                    }
//                    if (itemDetails.getItemCategory() != null) {
//                        item.setItemCategory(itemDetails.getItemCategory());
//                    }
//
//                    if (itemDetails.getItemQuantity() != null) {
//                        item.setItemQuantity(itemDetails.getItemQuantity());
//                    }
//                    if (itemDetails.getItemUrl() != null) {
//                        item.setItemUrl(itemDetails.getItemUrl());
//                    }
//
//                    if ( itemDetails.getIsFavorite() != (item.getIsFavorite()) ) {
//                        item.setIsFavorite(itemDetails.getIsFavorite());
//                    }
//
//                    Item updatedItem = itemRepository.save(item);
//                    return new ResponseEntity<>(updatedItem, HttpStatus.OK);
//                })
//                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

    // Update an Item with toggling
    @PatchMapping("/{id}")
    public ResponseEntity<Item> updateItemPartiall(@PathVariable Long id, @RequestBody Item itemDetails) {
        try {
            // Log req to check if the data is correct
            System.out.println("Received request to toggle favorite for item ID: " + id);

            // Fetch item by ID
            return itemRepository.findById(id)
                    .map(item -> {
                        // Log the current item data
                        System.out.println("Current item before update: " + item);

                        // Toggle favorite
                        if (itemDetails.getIsFavorite() != null) {
                            item.setIsFavorite(itemDetails.getIsFavorite());  // Set the value
                        }

                        // Save updates
                        Item updatedItem = itemRepository.save(item);

                        // Log updates
                        System.out.println("Updated item: " + updatedItem);
                        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
                    })
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            // Log the error
            System.err.println("Error toggling favorite: " + e.getMessage());
            e.printStackTrace();  // Print the full stack trace to the console
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 error response
        }
    }

    // Delete an item
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    itemRepository.delete(item);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
