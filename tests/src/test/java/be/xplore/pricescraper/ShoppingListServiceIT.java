package be.xplore.pricescraper;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.RecipeRepository;
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
import org.springframework.test.context.TestPropertySource;

@Import(IntegrationConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SpringBootTest(classes = ShoppingListServiceImpl.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:~/shoppingListServiceITDB;DB_CLOSE_DELAY=-1")
@ActiveProfiles("test")
class ShoppingListServiceIT {

  @Autowired
  ShoppingListRepository shoppingListRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ItemRepository itemRepository;
  @Autowired
  RecipeRepository recipeRepository;
  @Autowired
  ShoppingListServiceImpl shoppingListService;

  @BeforeEach
  void setup() {
    itemRepository.save(new Item());
    User user = new User();
    user.setUsername("test");
    userRepository.save(user);
  }

  @Test
  void shoppingListShouldBeAdded() {
    User user = userRepository.findById(1).get();
    ShoppingList shoppingList = new ShoppingList("mijn lijst", new ArrayList<>());
    shoppingListService.createShoppingListForUser(user, shoppingList);
    ShoppingList shoppingList1 = user.getShoppingLists().get(0);
    assertThat(shoppingList1.getTitle()).isEqualToIgnoringCase("mijn lijst");
    assertThat(user.getShoppingLists()).isNotEmpty();
  }

  @Test
  void shoppingListShouldBeDeleted() {
    User user = new User();
    user.setId(1L);
    user.setUsername("test");
    shoppingListService.createShoppingListForUser(user, new ShoppingList());
    shoppingListService.deleteShoppingListForUser(user, 0);
    assertThat(user.getShoppingLists()).isEmpty();
  }

  @Test
  void shoppingListShouldContainLineWithQuantity() {
    ShoppingList shoppingListSaved = shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(shoppingListSaved.getId(), 1, 2);
    ShoppingList shoppingList =
        shoppingListRepository.getShoppingListById(shoppingListSaved.getId()).get();
    assertThat(shoppingList.getLines().get(0).getQuantity()).isEqualTo(2);
  }

  @Test
  void shoppingListShouldContainItem() {
    ShoppingList shoppingListSaved = shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(shoppingListSaved.getId(), 1, 2);
    ShoppingList shoppingList =
        shoppingListRepository.getShoppingListById(shoppingListSaved.getId()).get();
    assertThat(shoppingList.getLines().get(0).getItem()).isNotNull();
  }

  @Test
  void shoppingListShouldBeEmpty() {
    ShoppingList shoppingListSaved = shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(shoppingListSaved.getId(), 1, 3);
    shoppingListService.deleteItemFromShoppingListById(shoppingListSaved.getId(), 1, 3);
    ShoppingList shoppingList =
        shoppingListRepository.getShoppingListById(shoppingListSaved.getId()).get();
    assertThat(shoppingList.getLines()).isEmpty();
  }

  @Test
  void shoppingListLineShouldHaveReducedQuantity() {
    ShoppingList shoppingListSaved = shoppingListRepository.save(new ShoppingList());
    shoppingListService.addItemToShoppingListById(shoppingListSaved.getId(), 1, 5);
    shoppingListService.deleteItemFromShoppingListById(shoppingListSaved.getId(), 1, 3);
    ShoppingList shoppingList =
        shoppingListRepository.getShoppingListById(shoppingListSaved.getId()).get();
    assertThat(shoppingList.getLines().get(0).getQuantity()).isEqualTo(2);
  }

  @Test
  void shouldCreateShoppingListFromRecipe() {
    User user = userRepository.findByUsernameWithShoppingLists("test").get();
    Recipe recipe = new Recipe("test recept", user);
    recipe = recipeRepository.save(recipe);
    shoppingListService.createShoppingListForUserFromRecipe(user, new ShoppingList(), recipe);
    user = userRepository.findByUsernameWithShoppingLists("test").get();
    assertThat(user.getShoppingLists()).isNotEmpty();
  }

}
