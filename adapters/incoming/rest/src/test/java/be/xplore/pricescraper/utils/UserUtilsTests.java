package be.xplore.pricescraper.utils;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.utils.users.UserUtils;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserUtilsTests {

  private User user1;
  private User user2;

  @BeforeAll
  void setup() {
    user1 = new User(1L, "test user 1", "",
        new ArrayList<>());
    user2 = new User(2L, "test user 2", "", null);
    user1.getShoppingLists().add(new ShoppingList(1, "lijst", null));

  }

  @Test
  void userShouldNotHaveOwnership() {
    boolean hasOwnership = UserUtils.userHasOwnershipOfShoppingList(user1, 2);
    assertThat(hasOwnership).isFalse();
  }

  @Test
  void userShouldNotHaveOwnershipWhenShoppingListIsNull() {
    boolean hasOwnership = UserUtils.userHasOwnershipOfShoppingList(user2, 1);
    assertThat(hasOwnership).isFalse();
  }

  @Test
  void userShouldHaveOwnership() {
    boolean hasOwnership = UserUtils.userHasOwnershipOfShoppingList(user1, 1);
    assertThat(hasOwnership).isTrue();
  }

}
