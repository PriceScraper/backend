package be.xplore.pricescraper.mocks;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.repositories.ShoppingListRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShoppingListInMemoryRepository implements ShoppingListRepository {
  private Map<Integer, ShoppingList> store = new HashMap<>();

  @Override
  public Optional<ShoppingList> getShoppingListById(int id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public ShoppingList save(ShoppingList shoppingList) {
    ShoppingList shoppingListInStore = store.get(shoppingList.getId());
    if (shoppingListInStore != null) {
      shoppingListInStore = shoppingList;
      return shoppingListInStore;
    }
    int newId = store.keySet().size() + 1;
    shoppingList.setId(newId);
    store.put(newId, shoppingList);
    return shoppingList;
  }
}
