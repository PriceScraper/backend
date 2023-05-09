package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.users.ShoppingListEntity;
import be.xplore.pricescraper.repositories.ShoppingListRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository implementation of {@link ShoppingListRepository}.
 */
public interface ShoppingListJpaRepository
    extends JpaRepository<ShoppingListEntity, Integer> {
  Optional<ShoppingListEntity> getShoppingListById(int id);
}
