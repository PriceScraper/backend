package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.users.RecurringShoppingListItem;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Repository interface.
 */
@Repository
public interface RecurringShoppingListItemRepository {
  /**
   * Delete by primary key.
   */
  void deleteById(long id);

  /**
   * Delete by primary key.
   */
  Optional<RecurringShoppingListItem> findById(long id);

  /**
   * Save entity.
   */
  RecurringShoppingListItem save(RecurringShoppingListItem entity);

  /**
   * Find all entities of user id.
   */
  List<RecurringShoppingListItem> findByUserId(long userId);
}
