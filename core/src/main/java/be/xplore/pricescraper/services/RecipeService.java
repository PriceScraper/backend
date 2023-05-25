package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.recipes.Recipe;
import be.xplore.pricescraper.domain.users.User;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Interface for service.
 */
@Service
public interface RecipeService {
  /**
   * Create new recipe.
   */
  Recipe add(String title, User user);

  /**
   * Get recipe if exists.
   */

  Optional<Recipe> get(long id);

  /**
   * Get all from creator.
   */

  List<Recipe> getByCreator(User user);

  /**
   * Get by filter.
   */

  List<Recipe> getByFilter(String filter);

  /**
   * Add item to recipe.
   */
  Optional<Recipe> addItemToRecipe(long recipeId, int itemId, User user);

  /**
   * Remove item from recipe.
   */

  Optional<Recipe> removeItemFromRecipe(long recipeId, int itemId, User user);
}
