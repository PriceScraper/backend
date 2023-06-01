package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.recipes.RecipeItem;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface.
 */
public interface RecipeItemRepository {
  /**
   * Save entity.
   */
  RecipeItem save(RecipeItem item);

  /**
   * Delete entity.
   */

  void deleteById(long id);

  /**
   * Find by recipe id.
   */

  List<RecipeItem> findItemsByRecipeId(long id);

  /**
   * Find linked item by item id and recipe id.
   */

  Optional<RecipeItem> findItemByRecipeIdAndItemId(long id, int itemId);
}
