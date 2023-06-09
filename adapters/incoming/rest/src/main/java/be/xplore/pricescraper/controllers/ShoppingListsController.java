package be.xplore.pricescraper.controllers;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.dtos.AddRecurringItemDto;
import be.xplore.pricescraper.dtos.RecurringItemDto;
import be.xplore.pricescraper.dtos.ShoppingListCreateDto;
import be.xplore.pricescraper.dtos.ShoppingListFromRecipeCreateDto;
import be.xplore.pricescraper.dtos.ShoppingListItemDto;
import be.xplore.pricescraper.exceptions.UnauthorizedActionExeption;
import be.xplore.pricescraper.services.ShoppingListService;
import be.xplore.pricescraper.services.UserService;
import be.xplore.pricescraper.utils.users.UserUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST controller for {@link be.xplore.pricescraper.domain.users.ShoppingList}.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("shoppinglists")
public class ShoppingListsController {

  private final UserService userService;
  private final ShoppingListService shoppingListService;

  /**
   * Get all shopping lists for {@link User}.
   */
  @GetMapping
  public List<ShoppingList> getShoppingListsForUser(
      @AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.loadUserByUsername(userDetails.getUsername());
    user.getShoppingLists()
        .forEach(shoppingListService::fillShoppingListWithTrackedItems);
    return user.getShoppingLists();
  }

  /**
   * Adds a {@link ShoppingList} to a {@link User}.
   *
   * @param dto         Dto for this action
   * @param userDetails The user details of the {@link User} that is currently authenticated
   */
  @PostMapping
  public void addShoppingList(@RequestBody ShoppingListCreateDto dto,
                              @AuthenticationPrincipal
                              UserDetails userDetails) {
    User user = userService.loadUserByUsername(userDetails.getUsername());
    ShoppingList shoppingList = new ShoppingList();
    shoppingList.setTitle(dto.title());
    shoppingListService.createShoppingListForUser(user, shoppingList);
  }

  /**
   * Adds a {@link ShoppingList} to a {@link User}.
   *
   * @param dto         Dto for this action
   * @param userDetails The user details of the {@link User} that is currently authenticated
   */
  @PostMapping("/recipe")
  public void addShoppingListFromRecipe(@RequestBody ShoppingListFromRecipeCreateDto dto,
                                        @AuthenticationPrincipal
                                        UserDetails userDetails) {
    User user = userService.loadUserByUsername(userDetails.getUsername());
    ShoppingList shoppingList = new ShoppingList();
    shoppingList.setTitle(dto.title());
    shoppingListService.createShoppingListForUserFromRecipe(user, shoppingList, dto.recipe());
  }

  /**
   * Deletes a {@link ShoppingList} from a {@link User}.
   *
   * @param id          Id of the {@link ShoppingList}
   * @param userDetails The userdetails of {@link User} that is currently authenticated
   */
  @DeleteMapping("/{id}")
  public void deleteShoppingList(@PathVariable int id,
                                 @AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.loadUserByUsername(userDetails.getUsername());
    if (!UserUtils.userHasOwnershipOfShoppingList(user, id)) {
      throw new UnauthorizedActionExeption();
    }
    shoppingListService.deleteShoppingListForUser(user, id);
  }

  /**
   * Adds an {@link be.xplore.pricescraper.domain.shops.Item} to a {@link ShoppingList}.
   *
   * @param dto         Dto for this action
   * @param userDetails The user details of the {@link User} that is currently authenticated
   */
  @PostMapping("/items/add")
  public void addItemToShoppingList(@RequestBody ShoppingListItemDto dto,
                                    @AuthenticationPrincipal
                                    UserDetails userDetails) {
    User user = userService.loadUserByUsername(userDetails.getUsername());
    if (!UserUtils.userHasOwnershipOfShoppingList(user, dto.shoppingListId())) {
      throw new UnauthorizedActionExeption();
    }
    shoppingListService.addItemToShoppingListById(dto.shoppingListId(), dto.itemId(),
        dto.quantity());
  }

  @PostMapping("/items/add/recurring")
  public void addRecurringItem(@RequestBody AddRecurringItemDto dto,
                               @AuthenticationPrincipal
                               UserDetails userDetails) {
    var user = userService.loadUserByUsername(userDetails.getUsername());
    shoppingListService.addRecurringItem(dto.itemId(), dto.quantity(), user);
  }

  @DeleteMapping("/items/remove/recurring/{id}")
  public void removeRecurringItem(@PathVariable int id,
                                  @AuthenticationPrincipal
                                  UserDetails userDetails) {
    var user = userService.loadUserByUsername(userDetails.getUsername());
    shoppingListService.removeRecurringItem(id, user);
  }

  /**
   * Get recurring items.
   */
  @GetMapping("/items/recurring")
  public ResponseEntity<List<RecurringItemDto>> getRecurringItems(
      @AuthenticationPrincipal UserDetails userDetails) {
    var user = userService.loadUserByUsername(userDetails.getUsername());
    var res = shoppingListService
        .getRecurringItemsFromUser(user).stream()
        .map(e -> new RecurringItemDto(e.getId(), e.getItem(), e.getQuantity()))
        .toList();
    return ResponseEntity.ok(res);
  }

  /**
   * Deletes an {@link be.xplore.pricescraper.domain.shops.Item} from a {@link ShoppingList}.
   *
   * @param dto         Dto for this action
   * @param userDetails The user details of the {@link User} that is currently authenticated
   */
  @PostMapping("/items/delete")
  public void deleteItemFromShoppingList(@RequestBody ShoppingListItemDto dto,
                                         @AuthenticationPrincipal
                                         UserDetails userDetails) {
    User user = userService.loadUserByUsername(userDetails.getUsername());
    if (!UserUtils.userHasOwnershipOfShoppingList(user, dto.shoppingListId())) {
      throw new UnauthorizedActionExeption();
    }
    shoppingListService.deleteItemFromShoppingListById(dto.shoppingListId(), dto.itemId(),
        dto.quantity());
  }

}
