package be.xplore.pricescraper.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.domain.recipes.RecipeItem;
import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.dtos.CreateRecipeDto;
import org.junit.jupiter.api.Test;

public class RecipeTests {
  @Test
  void recipe() {
    var recipe = new Recipe("title", null);
    assertEquals("title", recipe.getTitle());
    assertNull(recipe.getCreator());
  }

  @Test
  void recipeItem() {
    var recipe = new Recipe();
    var item = new Item();
    var entity = new RecipeItem(recipe, item, 3);
    assertEquals(recipe, entity.getRecipe());
    assertEquals(item, entity.getItem());
    assertEquals(3, entity.getQuantity());
  }

  @Test
  void createRecipeDto() {
    var dto = new CreateRecipeDto("title");
    assertEquals("title", dto.title());
  }
}
