package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.TrackedItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TrackedItemRepository.
 */
public interface TrackedItemRepository extends JpaRepository<TrackedItem, String> {
  boolean existsByUrlIgnoreCaseAndShop_Id(
      String url, int id);
}
