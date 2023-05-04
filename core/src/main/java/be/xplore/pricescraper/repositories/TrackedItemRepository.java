package be.xplore.pricescraper.repositories;

import be.xplore.pricescraper.domain.shops.TrackedItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link TrackedItem}.
 */
@Repository
public interface TrackedItemRepository {
  boolean existsByUrlIgnoreCaseAndShopId(String url, int id);

  Optional<TrackedItem> findByUrlIgnoreCaseAndShopId(String url, int id);

  TrackedItem save(TrackedItem trackedItem);

  long count();

  Page<TrackedItem> findAll(Pageable pageable);
}
