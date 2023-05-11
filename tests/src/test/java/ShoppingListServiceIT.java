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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@Import(IntegrationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
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

  @BeforeAll
  void setup() {
    itemRepository.save(new Item());
  }

  @Test
  void shouldReturnShoppingList() {
    ShoppingList shoppingListSaved = shoppingListRepository.save(new ShoppingList());
    ShoppingList shoppingList = shoppingListService.findShoppingListById(shoppingListSaved.getId());
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
    ShoppingList shoppingListSaved = shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(shoppingListSaved.getId(), 1, 2);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(shoppingListSaved.getId());
    assertThat(shoppingList.getLines().get(0).getQuantity()).isEqualTo(2);
  }

  @Test
  void shoppingListShouldContainItem() {
    ShoppingList shoppingListSaved = shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(shoppingListSaved.getId(), 1, 2);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(shoppingListSaved.getId());
    assertThat(shoppingList.getLines().get(0).getItem()).isNotNull();
  }

  @Test
  void shoppingListShouldBeEmpty() {
    ShoppingList shoppingListSaved = shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(shoppingListSaved.getId(), 1, 3);
    shoppingListService.deleteItemFromShoppingListById(shoppingListSaved.getId(), 1, 3);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(shoppingListSaved.getId());
    assertThat(shoppingList.getLines()).isEmpty();
  }

  @Test
  void shoppingListLineShouldHaveReducedQuantity() {
    ShoppingList shoppingListSaved = shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(shoppingListSaved.getId(), 1, 5);
    shoppingListService.deleteItemFromShoppingListById(shoppingListSaved.getId(), 1, 3);
    ShoppingList shoppingList = shoppingListService.findShoppingListById(shoppingListSaved.getId());
    assertThat(shoppingList.getLines().get(0).getQuantity()).isEqualTo(2);
  }

}
