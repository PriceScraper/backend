package be.xplore.pricescraper.dtos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.xplore.pricescraper.domain.recipes.Recipe;
import org.junit.jupiter.api.Test;

class RecipeTests {
  @Test
  void modifyRecipeItemDto() {
    var entity = new ModifyRecipeItemDto(1, 2);
    assertEquals(1, entity.recipeId());
    assertEquals(2, entity.itemId());
  }

  @Test
  void shoppingListFromRecipeCreateDto() {
    var entity = new ShoppingListFromRecipeCreateDto("title", new Recipe());
    assertEquals("title", entity.title());
    assertNotNull(entity.recipe());
  }
}
