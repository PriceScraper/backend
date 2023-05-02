package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.users.ShoppingListLine;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud repository for {@link ShoppingListLine}.
 */
public interface ShoppingListLineRepository extends CrudRepository<ShoppingListLine, Integer> {
}
