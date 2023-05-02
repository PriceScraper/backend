package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.ShoppingListLine;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.exceptions.ShoppingListItemNotFoundException;
import be.xplore.pricescraper.exceptions.ShoppingListNotFoundException;
import be.xplore.pricescraper.repositories.ShoppingListRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for {@link ShoppingList} operations.
 */
@Service
@RequiredArgsConstructor
public class ShoppingListServiceImpl implements ShoppingListService {

  private final ShoppingListRepository shoppingListRepository;
  private final ItemService itemService;

  public ShoppingList findShoppingListById(int id) {
    return shoppingListRepository.getShoppingListById(id)
        .orElseThrow(ShoppingListNotFoundException::new);
  }

  /**
   * Creates a {@link ShoppingList} for a {@link User}.
   *
   * @param user         the corresponding {@link User}
   * @param shoppingList the {@link ShoppingList} to add
   */
  @Transactional
  public void createShoppingListForUser(User user, ShoppingList shoppingList) {
    List<ShoppingList> shoppingLists =
        user.getShoppingLists() == null ? new ArrayList<>() : user.getShoppingLists();
    shoppingLists.add(shoppingList);
    user.setShoppingLists(shoppingLists);
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


}
