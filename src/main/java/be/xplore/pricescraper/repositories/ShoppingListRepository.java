package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.users.ShoppingList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for {@link ShoppingList}.
 */
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Integer> {

  Optional<ShoppingList> getShoppingListById(int id);

}
