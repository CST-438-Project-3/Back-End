package group15.pantrypal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pantries")
public class PantryController {

    @Autowired
    private PantryRepository pantryRepository;

    // Create a new pantry
    @PostMapping
    public ResponseEntity<Pantry> createPantry(@RequestBody Pantry pantry) {
        Pantry savedPantry = pantryRepository.save(pantry);
        return new ResponseEntity<>(savedPantry, HttpStatus.CREATED);
    }

    // Get all pantries
    @GetMapping
    public ResponseEntity<List<Pantry>> getAllPantries() {
        List<Pantry> pantries = pantryRepository.findAll();
        return new ResponseEntity<>(pantries, HttpStatus.OK);
    }

    // Get all pantries by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Pantry>> getPantriesByUserId(@PathVariable Long userId) {
        List<Pantry> pantries = pantryRepository.findByUserId(userId);
        return new ResponseEntity<>(pantries, HttpStatus.OK);
    }

    // Get a pantry by ID
    @GetMapping("/{id}")
    public ResponseEntity<Pantry> getPantryById(@PathVariable Long id) {
        return pantryRepository.findById(id)
                .map(pantry -> new ResponseEntity<>(pantry, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a pantry
    @PutMapping("/{id}")
    public ResponseEntity<Pantry> updatePantry(@PathVariable Long id, @RequestBody Pantry pantryDetails) {
        return pantryRepository.findById(id)
                .map(pantry -> {
                    pantry.setPantryName(pantryDetails.getPantryName());
                    Pantry updatedPantry = pantryRepository.save(pantry);
                    return new ResponseEntity<>(updatedPantry, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete a pantry
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePantry(@PathVariable Long id) {
        if (pantryRepository.existsById(id)) {
            pantryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
