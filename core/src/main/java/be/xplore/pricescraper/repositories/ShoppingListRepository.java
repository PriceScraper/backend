package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.users.ShoppingList;
import java.util.Optional;

/**
 * Repository for {@link ShoppingList}.
 */
public interface ShoppingListRepository {

  Optional<ShoppingList> getShoppingListById(int id);

  ShoppingList save(ShoppingList shoppingList);

}
