package be.xplore.pricescraper.utils.users;

import be.xplore.pricescraper.domain.users.User;

/**
 * This class exposes utility methods for {@link User}.
 */
public class UserUtils {

  private UserUtils() {
  }

  public static boolean userHasOwnershipOfShoppingList(User user, int shoppingListId) {
    return user.getShoppingLists() != null && user.getShoppingLists().stream()
        .anyMatch(shoppingList -> shoppingList.getId() == shoppingListId);
  }

}
