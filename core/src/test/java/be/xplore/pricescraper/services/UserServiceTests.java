package be.xplore.pricescraper.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.ShoppingListLine;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.repositories.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
  @Mock
  UserRepository userRepository;
  @InjectMocks
  UserServiceImpl userService;

  @BeforeEach
  void setup() {
    User testUser = new User();
    testUser.setShoppingLists(new ArrayList<>());
    testUser.getShoppingLists().add(new ShoppingList());
    testUser.getShoppingLists().get(0).setLines(new ArrayList<>());
    ShoppingListLine shoppingListLine = new ShoppingListLine(1, 1, null);
    testUser.getShoppingLists().get(0).getLines().add(shoppingListLine);
    testUser.getShoppingLists().get(0).getLines().add(shoppingListLine);
    when(userRepository.findByUsernameWithShoppingLists(any(String.class))).thenReturn(
        Optional.of(testUser));
  }

  @Test
  void loadByUsernameShouldReturnShoppingListWithUniqueLines() {
    User user = userService.loadUserByUsername("test");
    assertThat(user.getShoppingLists().get(0).getLines()).hasSize(1);
  }

}
