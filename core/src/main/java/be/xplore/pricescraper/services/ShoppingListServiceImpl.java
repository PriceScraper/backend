package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.users.RecurringShoppingListItem;
import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.ShoppingListLine;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.exceptions.ShoppingListItemNotFoundException;
import be.xplore.pricescraper.exceptions.ShoppingListNotFoundException;
import be.xplore.pricescraper.repositories.RecurringShoppingListItemRepository;
import be.xplore.pricescraper.repositories.ShoppingListRepository;
import be.xplore.pricescraper.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for {@link ShoppingList} operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingListServiceImpl implements ShoppingListService {

  private final ShoppingListRepository shoppingListRepository;
  private final ItemService itemService;
  private final UserRepository userRepository;
  private final RecurringShoppingListItemRepository recurringShoppingListItemRepository;

  public ShoppingList findShoppingListById(int id) {
    return shoppingListRepository.getShoppingListById(id)
        .orElseThrow(ShoppingListNotFoundException::new);
  }

  /**
   * Find {@link be.xplore.pricescraper.domain.shops.TrackedItem} for Items in {@link ShoppingList}.
   */
  public void fillShoppingListWithTrackedItems(ShoppingList shoppingList) {
    shoppingList.getLines().forEach(line -> line.setItem(
        itemService.findItemWithTrackedItemsAndLatestPriceById(line.getItem().getId())));
  }

  /**
   * Creates a {@link ShoppingList} for a {@link User}.
   *
   * @param user         the corresponding {@link User}
   * @param shoppingList the {@link ShoppingList} to add
   */
  @Transactional
  public void createShoppingListForUser(User user, ShoppingList shoppingList) {
    if (user.getShoppingLists() == null) {
      user.setShoppingLists(new ArrayList<>());
    }
    var recurringItems = recurringShoppingListItemRepository.findByUserId(user.getId());
    shoppingList.setLines(
        recurringItems.stream().map(r -> new ShoppingListLine(r.getQuantity(), r.getItem()))
            .toList());
    user.getShoppingLists().add(shoppingList);
    userRepository.save(user);
  }

  /**
   * Creates a {@link ShoppingList} for a {@link User}.
   *
   * @param user         the corresponding {@link User}
   * @param shoppingList the {@link ShoppingList} to add
   */
  @Transactional
  public void createShoppingListForUserFromRecipe(User user, ShoppingList shoppingList,
                                                  Recipe recipe) {
    if (user.getShoppingLists() == null) {
      user.setShoppingLists(new ArrayList<>());
    }
    var recurringItems = recurringShoppingListItemRepository.findByUserId(user.getId());
    var items = new ArrayList<>(
        recurringItems.stream().map(r -> new ShoppingListLine(r.getQuantity(), r.getItem()))
            .toList());
    recipe.getItems()
        .forEach(item -> items.add(new ShoppingListLine(item.getQuantity(), item.getItem())));
    shoppingList.setLines(items);
    user.getShoppingLists().add(shoppingList);
    userRepository.save(user);
  }

  /**
   * Deletes a {@link ShoppingList} from a {@link User}.
   *
   * @param user           the corresponding {@link User}
   * @param shoppingListId the id of {@link ShoppingList} to delete
   */
  @Transactional
  public void deleteShoppingListForUser(User user, int shoppingListId) {
    user.getShoppingLists().remove(user.getShoppingLists().stream()
        .filter(shoppingList -> shoppingList.getId() == shoppingListId).findFirst()
        .orElseThrow(ShoppingListNotFoundException::new));
    userRepository.save(user);
  }

  /**
   * Add an {@link Item} to a {@link ShoppingList}.
   *
   * @param id     Id of {@link ShoppingList}
   * @param itemId Id of {@link Item}
   */
  @Transactional
  public void addItemToShoppingListById(int id, int itemId, int quantity) {
    ShoppingList shoppingList = findShoppingListById(id);
    if (shoppingList.getLines() == null) {
      shoppingList.setLines(new ArrayList<>());
    }
    addItemToShoppingListLines(shoppingList.getLines(), itemId, quantity);
    shoppingListRepository.save(shoppingList);
  }

  private void addItemToShoppingListLines(List<ShoppingListLine> lines, int itemId, int quantity) {
    if (isItemInShoppingListLines(lines, itemId)) {
      addExistingItemToShoppingListLines(lines, itemId, quantity);
    } else {
      Item item = itemService.findItemById(itemId);
      addNewItemToShoppingListLines(lines, item, quantity);
    }
  }

  private boolean isItemInShoppingListLines(List<ShoppingListLine> shoppingListLines, int itemId) {
    return shoppingListLines.stream().anyMatch(line -> line.getItem().getId() == itemId);
  }

  private void addExistingItemToShoppingListLines(List<ShoppingListLine> lines, int itemId,
                                                  int quantity) {
    ShoppingListLine line = findLineInShoppingListLinesByItemId(lines, itemId);
    line.setQuantity(line.getQuantity() + quantity);
  }

  private void addNewItemToShoppingListLines(List<ShoppingListLine> lines, Item item,
                                             int quantity) {
    ShoppingListLine line = new ShoppingListLine();
    line.setItem(item);
    line.setQuantity(quantity);
    lines.add(line);
  }

  /**
   * Deletes an {@link Item} from a {@link ShoppingList}.
   *
   * @param id       Id of the {@link ShoppingList}.
   * @param itemId   Id of the {@link Item}.
   * @param quantity Quantity of the {@link Item}.
   */
  @Transactional
  public void deleteItemFromShoppingListById(int id, int itemId, int quantity) {
    ShoppingList shoppingList = findShoppingListById(id);
    deleteItemFromShoppingListLines(shoppingList.getLines(), itemId, quantity);
    shoppingListRepository.save(shoppingList);
  }


  private void deleteItemFromShoppingListLines(List<ShoppingListLine> lines, int itemId,
                                               int quantity) {
    ShoppingListLine line = findLineInShoppingListLinesByItemId(lines, itemId);
    if (line.getQuantity() > quantity) {
      line.setQuantity(line.getQuantity() - quantity);
    } else {
      lines.remove(line);
    }
  }

  private ShoppingListLine findLineInShoppingListLinesByItemId(
      List<ShoppingListLine> shoppingListLines,
      int itemId) {
    return shoppingListLines.stream().filter(line -> line.getItem().getId() == itemId)
        .findFirst().orElseThrow(ShoppingListItemNotFoundException::new);
  }

  @Override
  public void addRecurringItem(int itemId, int quantity, User user) {
    var item = itemService.findItemById(itemId);
    var temp = new RecurringShoppingListItem(item, quantity, user);
    var saved = recurringShoppingListItemRepository.save(temp);
    user.getShoppingLists().forEach(list -> addRecurringItemToShoppingList(list, saved));
  }

  @Override
  public void removeRecurringItem(int recurringItemId, User user) {
    var entity = recurringShoppingListItemRepository.findById(recurringItemId);
    if (entity.isEmpty()) {
      log.debug("Could not find recurring item by id " + recurringItemId);
      return;
    }
    if (!entity.get().getUser().getId().equals(user.getId())) {
      log.warn("Attempted to remove recurring item of other user.");
    }
    recurringShoppingListItemRepository.deleteById(recurringItemId);
  }

  @Override
  public List<RecurringShoppingListItem> getRecurringItemsFromUser(User user) {
    return recurringShoppingListItemRepository.findByUserId(user.getId());
  }

  private void addRecurringItemToShoppingList(ShoppingList list,
                                              RecurringShoppingListItem recurringShoppingListItem) {
    var lines = list.getLines();
    var line = new ShoppingListLine(recurringShoppingListItem.getQuantity(),
        recurringShoppingListItem.getItem());
    lines.add(line);
    list.setLines(lines);
    shoppingListRepository.save(list);
  }

}
