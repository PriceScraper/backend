package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.repositories.implementations.UserRepositoryImpl;
import be.xplore.pricescraper.utils.ModelMapperUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({UserRepositoryImpl.class, ModelMapperUtil.class})
class UserRepositoryTests {
  @Autowired
  UserRepositoryImpl userRepository;

  @BeforeAll
  void setup() {
    User user = userRepository.save(new User("test", "testprovider"));
    List<ShoppingList> shoppingLists = new ArrayList<>();
    shoppingLists.add(new ShoppingList());
    user.setShoppingLists(shoppingLists);
    userRepository.save(user);
  }

  @Test
  void findByUsernameAndProviderShouldReturnUser() {
    Optional<User> user = userRepository.findByUsernameAndProvider("test", "testprovider");
    assertThat(user).isPresent();
  }

  @Test
  void userShouldHaveShoppingLists() {
    Optional<User> user = userRepository.findByUsernameWithShoppingLists("test");
    assertThat(user).isPresent();
    assertThat(user.get().getShoppingLists()).hasSize(1);
  }

  @Test
  void findByIdShouldReturnUser() {
    Optional<User> user = userRepository.findById(1L);
    assertThat(user).isPresent();
  }

  @Test
  void userShouldSave() {
    User user = userRepository.save(new User());
    assertThat(user.getId()).isPositive();
  }

}
