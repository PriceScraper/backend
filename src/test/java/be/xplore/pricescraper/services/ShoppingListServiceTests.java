package be.xplore.pricescraper.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.exceptions.ShoppingListNotFoundException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
@Import({ShoppingListService.class, ItemService.class, UserService.class, ScraperService.class})
@Sql({"/sql/shopping_list_test_data.sql"})
class ShoppingListServiceTests {

  @Autowired
  ShoppingListService shoppingListService;
  @Autowired
  UserService userService;

  @Test
  void shouldReturnShoppingList() {
    boolean exists = shoppingListService.findShoppingListById(1) != null;
    assertThat(exists).isTrue();
  }

  @Test
  void shoppingListShouldBeAdded() {
    User user = userService.loadUserByUsername("test");
    ShoppingList shoppingList = new ShoppingList(2, "mijn lijst", new ArrayList<>());
    shoppingListService.createShoppingListForUser(user, shoppingList);
    ShoppingList shoppingList1 = shoppingListService.findShoppingListById(2);
    assertThat(shoppingList1.getTitle()).isEqualToIgnoringCase("mijn lijst");
    assertThat(user.getShoppingLists()).isNotEmpty();
  }

  @Test
  void shoppingListShouldBeDeleted() {
    User user = userService.loadUserByUsername("test");
    shoppingListService.createShoppingListForUser(user, new ShoppingList());
    shoppingListService.deleteShoppingListForUser(user, 0);
    assertThatThrownBy(() -> shoppingListService.findShoppingListById(0)).isInstanceOf(
        ShoppingListNotFoundException.class);
  }

  @Test
  void shoppingListShouldContainLineWithQuantity() {
    shoppingListService.addItemToShoppingListById(1, 1, 2);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(1);
    assertThat(shoppingList.getLines().get(0).getQuantity()).isEqualTo(2);
  }

  @Test
  void shoppingListShouldContainItem() {
    shoppingListService.addItemToShoppingListById(1, 1, 2);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(1);
    assertThat(shoppingList.getLines().get(0).getItem()).isNotNull();
  }

  @Test
  void shoppingListShouldBeEmpty() {
    shoppingListService.addItemToShoppingListById(1, 1, 3);
    shoppingListService.deleteItemFromShoppingListById(1, 1, 3);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(1);
    assertThat(shoppingList.getLines()).isEmpty();
  }

  @Test
  void shoppingListLineShouldHaveReducedQuantity() {
    shoppingListService.addItemToShoppingListById(1, 1, 5);
    shoppingListService.deleteItemFromShoppingListById(1, 1, 3);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(1);
    assertThat(shoppingList.getLines().get(0).getQuantity()).isEqualTo(2);
  }

}
