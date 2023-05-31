package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.domain.users.RecurringShoppingListItem;
import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.User;
import java.util.List;

/**
 * Service for {@link ShoppingList} operations.
 */
public interface ShoppingListService {
  void fillShoppingListWithTrackedItems(ShoppingList shoppingList);

  void createShoppingListForUser(User user, ShoppingList shoppingList);

  void createShoppingListForUserFromRecipe(User user, ShoppingList shoppingList, Recipe recipe);

  void deleteShoppingListForUser(User user, int shoppingListId);

  void addItemToShoppingListById(int id, int itemId, int quantity);

  void deleteItemFromShoppingListById(int id, int itemId, int quantity);

  void addRecurringItem(int itemId, int quantity, User user);

  void removeRecurringItem(int recurringItemId, User user);

  List<RecurringShoppingListItem> getRecurringItemsFromUser(User user);
}
