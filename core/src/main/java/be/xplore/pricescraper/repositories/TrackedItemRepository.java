package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.TrackedItem;
import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link TrackedItem}.
 */
public interface TrackedItemRepository {
  boolean existsByUrlIgnoreCaseAndShopId(String url, int id);

  Optional<TrackedItem> findByUrlIgnoreCaseAndShopId(String url, int id);

  TrackedItem save(TrackedItem trackedItem);

  long count();

  List<TrackedItem> findAll(int limit);

  List<TrackedItem> findAllSortedByOldestFirst(int limit);

  boolean existsByUrlIgnoreCase(String url);
}
