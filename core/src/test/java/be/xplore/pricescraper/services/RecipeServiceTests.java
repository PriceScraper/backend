package be.xplore.pricescraper.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.domain.recipes.RecipeItem;
import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.RecipeItemRepository;
import be.xplore.pricescraper.repositories.RecipeRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {RecipeServiceImpl.class})
public class RecipeServiceTests {
  @Autowired
  private RecipeService service;
  @MockBean
  private RecipeRepository recipeRepository;
  @MockBean
  private ItemRepository itemRepository;
  @MockBean
  private RecipeItemRepository recipeItemRepository;

  @BeforeEach
  void prepare() {
    var recipe = new Recipe(1, "title", new User(), new ArrayList<>());
    given(recipeRepository.save(any()))
        .willReturn(recipe);

    given(recipeRepository.findById(anyLong()))
        .willReturn(Optional.of(recipe));

    var list = new ArrayList<RecipeItem>();
    list.add(new RecipeItem(1, new Recipe(), new Item(), 1));
    given(recipeItemRepository.findItemsByRecipeId(anyLong()))
        .willReturn(list);

  }

  @Test
  void add() {
    var entity = service.add("title", null);
    assertNotNull(entity);
    assertTrue(entity.getId() > 0);
  }

  @Test
  void get() {
    var entity = service.get(1);
    assertTrue(entity.isPresent());
    assertTrue(entity.get().getItems().size() > 0);
  }
}
