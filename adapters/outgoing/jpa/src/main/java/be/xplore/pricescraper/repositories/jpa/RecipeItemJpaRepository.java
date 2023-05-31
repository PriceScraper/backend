package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.recipe.RecipeItemEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa repository.
 */
public interface RecipeItemJpaRepository extends JpaRepository<RecipeItemEntity, Long> {
  /**
   * Find by recipe pk and item pk.
   */
  Optional<RecipeItemEntity> findByRecipeIdAndItemId(long id, int id1);

  /**
   * Find by recipe pk.
   */
  List<RecipeItemEntity> findByRecipeId(long id);
}
