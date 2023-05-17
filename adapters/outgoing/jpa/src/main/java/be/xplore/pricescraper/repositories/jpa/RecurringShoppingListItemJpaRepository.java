package be.xplore.pricescraper.repositories.jpa;

import be.xplore.pricescraper.entity.users.RecurringShoppingListItemEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Jpa repository for entity.
 */
public interface RecurringShoppingListItemJpaRepository extends
    JpaRepository<RecurringShoppingListItemEntity, Long> {
  List<RecurringShoppingListItemEntity> findByUser_Id(Long id);

}
