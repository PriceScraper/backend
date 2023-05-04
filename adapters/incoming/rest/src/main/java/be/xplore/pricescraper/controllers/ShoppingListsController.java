package be.xplore.pricescraper.controllers;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.dtos.ShoppingListCreateDto;
import be.xplore.pricescraper.dtos.ShoppingListDeleteDto;
import be.xplore.pricescraper.dtos.ShoppingListItemDto;
import be.xplore.pricescraper.exceptions.UnauthorizedActionExeption;
import be.xplore.pricescraper.services.ShoppingListService;
import be.xplore.pricescraper.services.UserService;
import be.xplore.pricescraper.utils.users.UserUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping
  public List<ShoppingList> getShoppingListsForUser(
      @AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.loadUserByUsername(userDetails.getUsername());
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
   * Deletes a {@link ShoppingList} from a {@link User}.
   *
   * @param dto         Dto for this action
   * @param userDetails The userdetails of {@link User} that is currently authenticated
   */
  @DeleteMapping("")
  public void deleteShoppingList(@RequestBody ShoppingListDeleteDto dto,
                                 @AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.loadUserByUsername(userDetails.getUsername());
    if (!UserUtils.userHasOwnershipOfShoppingList(user, dto.id())) {
      throw new UnauthorizedActionExeption();
    }
    shoppingListService.deleteShoppingListForUser(user, dto.id());
  }

  /**
   * Adds an {@link be.xplore.pricescraper.domain.shops.Item} to a {@link ShoppingList}.
   *
   * @param dto         Dto for this action
   * @param userDetails The user details of the {@link User} that is currently authenticated
   */
  @PostMapping("/items")
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

  /**
   * Deletes an {@link be.xplore.pricescraper.domain.shops.Item} from a {@link ShoppingList}.
   *
   * @param dto         Dto for this action
   * @param userDetails The user details of the {@link User} that is currently authenticated
   */
  @DeleteMapping("/items")
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
