package group15.pantrypal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemsRepository extends JpaRepository<UserItems, Long> {
    List<UserItems> findByUserId(Long userId);
}
