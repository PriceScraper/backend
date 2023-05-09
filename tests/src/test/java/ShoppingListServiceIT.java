import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.exceptions.ShoppingListNotFoundException;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.ShoppingListRepository;
import be.xplore.pricescraper.repositories.UserRepository;
import be.xplore.pricescraper.services.ShoppingListServiceImpl;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@Import(IntegrationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = ShoppingListServiceImpl.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ShoppingListServiceIT {

  @Autowired
  ShoppingListRepository shoppingListRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ItemRepository itemRepository;
  @Autowired
  ShoppingListServiceImpl shoppingListService;

  @BeforeEach
  void setup() {
    shoppingListRepository.save(new ShoppingList());
    itemRepository.save(new Item());
  }

  @Test
  void shouldReturnShoppingList() {
    ShoppingList shoppingList = shoppingListService.findShoppingListById(1);
    assertThat(shoppingList).isNotNull();
  }

  @Test
  void shoppingListShouldBeAdded() {
    User user = new User();
    user.setUsername("test");
    user = userRepository.save(user);
    ShoppingList shoppingList = new ShoppingList(1, "mijn lijst", new ArrayList<>());
    shoppingListService.createShoppingListForUser(user, shoppingList);
    ShoppingList shoppingList1 = user.getShoppingLists().get(0);
    assertThat(shoppingList1.getTitle()).isEqualToIgnoringCase("mijn lijst");
    assertThat(user.getShoppingLists()).isNotEmpty();
  }

  @Test
  void shoppingListShouldBeDeleted() {
    User user = new User();
    user.setUsername("test");
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
    shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(1, 1, 2);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(1);
    assertThat(shoppingList.getLines().get(0).getItem()).isNotNull();
  }

  @Test
  void shoppingListShouldBeEmpty() {
    shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(1, 1, 3);
    shoppingListService.deleteItemFromShoppingListById(1, 1, 3);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(1);
    assertThat(shoppingList.getLines()).isEmpty();
  }

  @Test
  void shoppingListLineShouldHaveReducedQuantity() {
    shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(1, 1, 5);
    shoppingListService.deleteItemFromShoppingListById(1, 1, 3);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(1);
    assertThat(shoppingList.getLines().get(0).getQuantity()).isEqualTo(2);
  }

}
