package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.recipes.Recipe;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;


/**
 * Repository interface.
 */
@Repository
public interface RecipeRepository {
  /**
   * Save entity.
   */
  Recipe save(Recipe recipe);

  /**
   * Find lists by creator.
   */
  List<Recipe> findByCreatorId(long userId);

  Optional<Recipe> findById(long id);

  List<Recipe> findByTitle(String filter);

  /**
   * Get amount of rows.
   */

  long count();
}
