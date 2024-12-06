package group15.pantrypal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PantryRepository extends JpaRepository<Pantry, Long> {
    List<Pantry> findByUserId(Long userId);
}
