package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.recipe.RecipeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Jpa repository.
 */
@Repository
public interface RecipeJpaRepository extends JpaRepository<RecipeEntity, Long> {
  /**
   * Filter.
   */
  List<RecipeEntity> findByTitleContainsIgnoreCase(String title);

  /**
   * Find by creator pk.
   */

  List<RecipeEntity> findByCreator_Id(Long id);
}
