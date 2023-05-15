package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.User;

/**
 * Service for {@link ShoppingList} operations.
 */
public interface ShoppingListService {

  ShoppingList findShoppingListById(int id);

  void fillShoppingListWithTrackedItems(ShoppingList shoppingList);

  void createShoppingListForUser(User user, ShoppingList shoppingList);

  void deleteShoppingListForUser(User user, int shoppingListId);

  void addItemToShoppingListById(int id, int itemId, int quantity);

  void deleteItemFromShoppingListById(int id, int itemId, int quantity);
}
