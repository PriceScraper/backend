package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.repositories.implementations.RecipeRepositoryImpl;
import be.xplore.pricescraper.repositories.implementations.UserRepositoryImpl;
import be.xplore.pricescraper.utils.ModelMapperUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:~/testdb;DB_CLOSE_DELAY=-1")
@Import({RecipeRepositoryImpl.class, UserRepositoryImpl.class, ModelMapperUtil.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeRepositoryTests {
  @Autowired
  RecipeRepositoryImpl recipeRepository;
  @Autowired
  UserRepositoryImpl userRepository;

  @BeforeAll
  void setup() {
    User user = new User("test user", "", "");
    user = userRepository.save(user);
    Recipe recipe = new Recipe("test recept", user);
    recipeRepository.save(recipe);
  }

  @Test
  void userShouldHaveRecipe() {
    List<Recipe> recipes = recipeRepository.findByCreatorId(1);
    assertThat(recipes).isNotEmpty();
  }
}