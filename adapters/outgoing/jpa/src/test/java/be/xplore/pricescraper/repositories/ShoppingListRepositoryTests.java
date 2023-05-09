package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.users.ShoppingList;
import be.xplore.pricescraper.repositories.implementations.ShoppingListRepositoryImpl;
import be.xplore.pricescraper.utils.ModelMapperUtil;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({ShoppingListRepositoryImpl.class, ModelMapperUtil.class})
class ShoppingListRepositoryTests {
  @Autowired
  ShoppingListRepositoryImpl shoppingListRepository;

  @BeforeAll
  void setup() {
    shoppingListRepository.save(new ShoppingList());
  }

  @Test
  void getByIdShouldReturnShoppingList() {
    Optional<ShoppingList> shoppingList = shoppingListRepository.getShoppingListById(1);
    assertThat(shoppingList.isPresent()).isTrue();
  }

  @Test
  void shoppingListShouldHaveIdAfterSaving() {
    ShoppingList shoppingList = new ShoppingList();
    shoppingList = shoppingListRepository.save(shoppingList);
    assertThat(shoppingList.getId()).isGreaterThan(0);
  }

}
